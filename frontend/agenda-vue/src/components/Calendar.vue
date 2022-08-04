<template>
  <div class="q-my-md q-py-md">
    <q-card
      flat
      bordered
      class="row text-center"
      v-if="events"
      style="height: 1000px"
    >
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
        v-for="[date, dayEvents] in events"
        :key="date"
        style="height: 970px; position: relative"
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
          <q-icon v-if="dayEvents.length == 0" name="remove" size="xl" />
          <q-card
            v-for="event in dayEvents"
            :key="event.id"
            :flat="event.eventType == 'AVAILABILITY'"
            bordered
            :class="
              event.eventType == 'AVAILABILITY' ? 'bg-secondary' : 'bg-primary'
            "
            class="text-white"
            :style="calcStyle(event)"
          >
            <div v-if="event.eventType == 'APPOINTMENT'">
              {{ event.title }}
            </div>
            <q-tooltip>
              <div v-if="event.eventType == 'APPOINTMENT'">
                {{ event.time }} for {{ event.duration }} minutes
              </div>
              <div v-if="event.eventType == 'AVAILABILITY'">
                you are available from {{ event.time }} for
                {{ event.duration }} minutes
              </div>
            </q-tooltip>
          </q-card>
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
    currentUserId: Number,
  },
  mounted() {
    this.loadEvents();
  },
  setup(props) {
    var weekNumber = date.getWeekOfYear(new Date());
    const events = ref(null);
    const $q = useQuasar();

    function loadEvents() {
      if (props.currentUserId) {
        api
          .get("/agenda/week", {
            params: {
              userId: props.currentUserId,
              weekNumber: weekNumber,
            },
          })
          .then((response) => {
            events.value = new Map(Object.entries(response.data));
          })
          .catch(() => {
            $q.notify({
              color: "negative",
              position: "top",
              message: "Loading calendar failed",
              icon: "report_problem",
            });
          });
      } else {
        $q.notify({
          color: "negative",
          position: "top",
          message: "No user received!",
          icon: "report_problem",
        });
      }
    }
    function prevWeekLoad() {
      weekNumber--;
      loadEvents();
    }
    function nextWeekLoad() {
      weekNumber++;
      loadEvents();
    }
    function calcStyle(dayEvent) {
      console.debug(dayEvent);
      const marginTop = (dayEvent.time.split(":")[0] - 7) * 4 * 10 + 30;
      const style = {
        height: dayEvent.duration + "px",
        "margin-top": marginTop + "px",
        position: "absolute",
        width: dayEvent.eventType == "APPOINTMENT" ? "90%" : "40%",
      };
      console.debug(style);
      return style;
    }
    return {
      dayLength: "",
      calcStyle,
      loadEvents,
      events,

      prevWeekLoad,
      nextWeekLoad,
    };
  },
};
</script>
