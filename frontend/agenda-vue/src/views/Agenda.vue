<template>
  <div class="home">
    <Authentication v-if="!currentUserId"></Authentication>
    <q-list bordered class="rounded-borders">
      <q-expansion-item
        expand-separator
        icon="perm_identity"
        label="Availability settings"
        :caption="
          !currentUserId
            ? 'Please login (select a user)'
            : 'You can add availabilities here'
        "
        :disable="!currentUserId"
      >
        <q-card>
          <Availability
            v-if="currentUserId"
            :currentUserId="currentUserId"
          ></Availability>
        </q-card>
      </q-expansion-item>

      <q-expansion-item
        expand-separator
        icon="calendar_month"
        label="Calendar"
        :caption="
          !currentUserId
            ? 'Please login (select a user)'
            : 'You can view your availabilities and appointments'
        "
        :disable="!currentUserId"
      >
        <q-card>
          <Calendar
            v-if="currentUserId"
            :currentUserId="currentUserId"
          ></Calendar>
        </q-card>
      </q-expansion-item>
    </q-list>
  </div>
</template>

<script>
import Authentication from "../components/Authentication.vue";
import Availability from "../components/Availability.vue";
import Calendar from "../components/Calendar.vue";

export default {
  name: "Home",
  computed: {
    currentUserId() {
      return this.$store.state.currentUserId;
    },
  },
  components: {
    Authentication,
    Availability,
    Calendar,
  },
};
</script>
