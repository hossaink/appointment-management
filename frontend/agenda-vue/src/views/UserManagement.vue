<template>
  <div class="home">
    <user-list></user-list>
  </div>
</template>

<script>
import UserList from "../components/UserList.vue";

export default {
  name: "User Management",
  computed: {},
  components: {
    UserList,
  },
};
</script>
