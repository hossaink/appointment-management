<template>
  <div class="q-my-md q-py-md">
    <q-card flat bordered class="row text-center">
      <q-select
        filled
        v-model="selectedReceiver"
        :options="users"
        option-value="id"
        option-label="name"
        label="Whom you want to visit?"
        style="min-width: 85%"
      />
      <q-space />
      <q-btn
        icon="event"
        color="white"
        text-color="black"
        label="load Slots"
        @click="loadSlots()"
        style="min-width: 15%"
      />
    </q-card>

    <q-card flat bordered class="row text-center" v-if="slots">
      <q-icon
        class="col-1"
        name="arrow_back_ios"
        size="xl"
        @click="prevWeekLoad()"
      ></q-icon>

      <q-list
        bordered
        padding
        class="rounded-borders text-primary col text-center"
        v-for="[date, daySlots] in slots"
        :key="date"
      >
        <div class="bg-cyan text-white col text-center">
          <div class="text-h6">
            {{
              new Date(date).toLocaleDateString("en-US", { weekday: "long" })
            }}
          </div>
          <div class="text-subtitle1">
            {{
              new Date(date).toLocaleDateString("en-US", {
                year: "numeric",
                month: "long",
                day: "numeric",
              })
            }}
          </div>
        </div>
        <div class="q-ma-md">
          <q-icon v-if="daySlots.length == 0" name="remove" size="xl" />
          <q-btn
            v-for="slot in daySlots"
            :key="slot.id"
            class="glossy q-ma-sm"
            rounded
            :disable="!slot.noConflict || slot.booked"
            @click="bookAppointment(slot)"
          >
            {{ slot.startTime }}
            <q-tooltip>
              <div v-if="slot.noConflict && !slot.booked">
                {{ slot.startTime }} - {{ slot.endTime }}
              </div>

              <div v-if="!slot.noConflict">
                Conflict: You, yourself, are not available at this time.
              </div>

              <div v-if="slot.booked">Already booked</div>
            </q-tooltip>
          </q-btn>
        </div>
      </q-list>
      <q-icon
        class="col-1"
        name="arrow_forward_ios"
        size="xl"
        @click="nextWeekLoad"
      ></q-icon>
    </q-card>
  </div>
</template>

<script>
import { ref } from "vue";
import { api } from "@/boot/axios";
import { useQuasar, date } from "quasar";

export default {
  props: {
    allUsers: Array,
    currentUserId: Number,
  },
  setup(props) {
    var weekNumber = date.getWeekOfYear(new Date());
    const slots = ref(null);
    const $q = useQuasar();
    const selectedReceiver = ref(null);

    function loadSlots() {
      if (selectedReceiver.value) {
        api
          .get("/agenda/slots", {
            params: {
              receiverId: selectedReceiver.value.id,
              takerId: props.currentUserId,
              weekNumber: weekNumber,
            },
          })
          .then((response) => {
            slots.value = new Map(Object.entries(response.data));
          })
          .catch(() => {
            $q.notify({
              color: "negative",
              position: "top",
              message: "Loading slots failed",
              icon: "report_problem",
            });
          });
      } else {
        $q.notify({
          color: "negative",
          position: "top",
          message: "No user selected!",
          icon: "report_problem",
        });
      }
    }
    function prevWeekLoad() {
      weekNumber--;
      loadSlots();
    }
    function nextWeekLoad() {
      weekNumber++;
      loadSlots();
    }
    function bookAppointment(selectedSlot) {
      api
        .post(
          "/agenda/book",
          {},
          {
            params: {
              takerId: props.currentUserId,
              receiverId: selectedSlot.receiverUserId,
              dateStr: selectedSlot.date,
              startTimeStr: selectedSlot.startTime,
              endTimeStr: selectedSlot.endTime,
            },
          }
        )
        .then(() => {
          selectedSlot.booked = true;
          $q.notify({
            color: "positive",
            position: "top",
            message: "Successfully booked the appointment",
            icon: "tick",
          });
        })
        .catch(() => {
          $q.notify({
            color: "negative",
            position: "top",
            message: "Booking failed",
            icon: "report_problem",
          });
        });
    }

    return {
      selectedReceiver,
      users: props.allUsers,
      loadSlots,
      slots,
      bookAppointment,
      prevWeekLoad,
      nextWeekLoad,
    };
  },
};
</script>
