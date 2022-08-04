<template>
  <div class="q-pa-md row">
    <q-date
      mask="YYYY-MM-DD"
      v-if="repeat != 'NOT_REPEATED'"
      class="col q-ma-md"
      v-model="fromToDate"
      range
    />
    <q-date
      mask="YYYY-MM-DD"
      v-if="repeat == 'NOT_REPEATED'"
      class="col q-ma-md"
      v-model="singleDate"
    />
    <q-time
      class="col q-ma-md"
      :options="optionsFnStartTime"
      v-model="startTime"
      format24h
    />
    <q-time
      class="col q-ma-md"
      :options="optionsFnEndTime"
      v-model="endTime"
      format24h
    />
    <q-card class="col q-ma-md">
      <q-card-section>
        <q-list>
          <q-item>
            <q-radio
              v-model="repeat"
              checked-icon="task_alt"
              unchecked-icon="panorama_fish_eye"
              val="NOT_REPEATED"
              label="one-time"
            />
          </q-item>
          <q-item>
            <q-radio
              v-model="repeat"
              checked-icon="task_alt"
              unchecked-icon="panorama_fish_eye"
              val="DAILY"
              label="daily"
            />
          </q-item>
          <q-item>
            <q-radio
              v-model="repeat"
              checked-icon="task_alt"
              unchecked-icon="panorama_fish_eye"
              val="WEEKLY"
              label="weekly"
            />
          </q-item>
        </q-list>
      </q-card-section>
      <q-separator />
      <q-card-section>
        <q-card flat>
          <q-card-section>
            Duration:
            <q-badge color="secondary">
              {{ duration }}
            </q-badge>

            <q-slider v-model="duration" :min="15" :step="15" :max="120" />
          </q-card-section>
        </q-card>
      </q-card-section>
      <q-separator />
      <q-card-actions>
        <q-btn
          icon="save"
          class="glossy col"
          color="primary"
          @click="saveAvailability"
        >
          Save Availability
        </q-btn>
      </q-card-actions>
      <!-- <q-radio
          v-model="repeat"
          checked-icon="task_alt"
          unchecked-icon="panorama_fish_eye"
          disable
          val="MONTHLY"
          label="monthly"
        /> -->
    </q-card>
  </div>
</template>

<script>
import { ref } from "vue";
import { useQuasar } from "quasar";
import { api } from "../boot/axios";

export default {
  props: {
    allUsers: Array,
    currentUserId: Number,
  },
  setup(props) {
    const $q = useQuasar();

    var fromToDate = ref({
      from: "",
      to: "",
    });
    var startTime = ref("09:00");
    var endTime = ref("17:00");
    var duration = ref("60");
    var repeat = ref("NOT_REPEATED");
    var singleDate = ref(null);
    /*
       @RequestParam long userId,
            @RequestParam String startDateStr,
            @RequestParam  String startTimeStr,
            @RequestParam String endDateStr,
            @RequestParam String endTimeStr,
            @RequestParam String repeat,
            @RequestParam int length */
    function saveAvailability() {
      api
        .post(
          "/agenda/availability",
          {},
          {
            params: {
              userId: props.currentUserId,
              startDateStr:
                repeat.value == "NOT_REPEATED"
                  ? singleDate.value
                  : fromToDate.value.from,
              startTimeStr: startTime.value,
              endDateStr:
                repeat.value == "NOT_REPEATED"
                  ? singleDate.value
                  : fromToDate.value.to,
              endTimeStr: endTime.value,
              repeat: repeat.value,
              length: duration.value,
            },
          }
        )
        .then(() => {
          $q.notify({
            color: "positive",
            position: "top",
            message: "Successfully added new availability",
            icon: "tick",
          });
        })
        .catch(() => {
          $q.notify({
            color: "negative",
            position: "top",
            message: "Saving availability failed",
            icon: "report_problem",
          });
        });
    }
    return {
      fromToDate,
      startTime,
      endTime,
      repeat,
      duration,
      singleDate,
      saveAvailability,
      optionsFnStartTime(hr, min) {
        if (hr < 6 || hr > 21) return false;
        if (hr > endTime.value.split(":")[0]) return false;
        if (min !== null && min % 15 !== 0) return false;

        return true;
      },

      optionsFnEndTime(hr, min) {
        if (hr < 6 || hr > 21) return false;
        if (hr < startTime.value.split(":")[0]) return false;
        if (min !== null && min % 15 !== 0) return false;

        return true;
      },
    };
  },
};
</script>
