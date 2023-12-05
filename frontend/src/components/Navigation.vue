<template>
      <v-layout class="layout">
        <v-navigation-drawer
          class="bg-deep-purple drawer"
          theme="dark"
          width="100px"
          permanent
        >
          <v-list color="transparent" class="drawer-list">
            <v-btn icon title="Home" value="" size="default" v-on:click="goToHome"><homegroup class="home-icon"/></v-btn>
            <v-btn icon title="League" value="lol" size="default" v-on:click="goToLeague"><sizel class="league-icon"/></v-btn>
            <v-btn icon title="Sources" value="lol" size="default" v-on:click="goToSources">src</v-btn>
            <toggle-theme class="swap-themes" size="default"/>
          </v-list>
        </v-navigation-drawer>
        <v-main></v-main>
      </v-layout>
  </template>

<script setup lang="ts">
import router from "@/router/index";
import sizel from "vue-material-design-icons/SizeL.vue";
import homegroup from "vue-material-design-icons/HomeGroup.vue"
import ToggleTheme from "./ToggleTheme.vue";

function goToLeague() {
    router.push('/lol')
    .then(() => {

    });
}

function goToHome() {
    router.push('/')
    .then(() => {

    });
}

function goToSources() {
    router.push('/sources')
    .then(() => {

    });
}

</script>

<style>
.drawer {
  padding: 15px;
}
.layout {
  position: absolute;
  top:10px
}

.drawer-list {
  display:flex;
  flex-direction: column;
  gap: 10px;
}

.home-icon {
  margin-bottom: 7px;
  transform: scale(2);
}

.swap-themes {
  margin-top: 200%;
  margin-bottom: 7px;
}

.league-icon {
  margin-top: auto;
  margin-bottom: 13px;
  transform: scale(3);
}

</style>
