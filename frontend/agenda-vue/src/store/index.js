import { createStore } from "vuex";

export default createStore({
  state() {
    return {
      currentUserId: 0,
      currentUserName: "",
      allUsers: [],
    };
  },
  getters: {
    currentUserName: (state) => state.currentUserName,
  },
  mutations: {
    login(state, user) {
      state.currentUserId = user.id;
      state.currentUserName = user.name;
      console.log(user);
    },
    updateAllUsers(state, allUsers) {
      state.allUsers = allUsers;
      console.log(state);
    },
  },
  actions: {},
  modules: {},
});
