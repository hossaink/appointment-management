<template>
  <div class="home">
    <Authentication v-if="!currentUserId"></Authentication>
    <AppointmentSelection
      v-if="currentUserId"
      :currentUserId="currentUserId"
      :allUsers="allUsers"
    ></AppointmentSelection>
  </div>
</template>

<script>
import Authentication from "../components/Authentication.vue";
import AppointmentSelection from "../components/AppointmentSelection.vue";

export default {
  name: "Home",
  computed: {
    currentUserId() {
      return this.$store.state.currentUserId;
    },
    allUsers() {
      console.debug(this.$store.state.allUsers);
      return this.$store.state.allUsers;
    },
  },
  components: {
    Authentication,
    AppointmentSelection,
  },
};
</script>
