<template>
    <v-btn icon title="Theme" size="x-large" @click="switchTheme"><themeIcon class="theme-icon"/></v-btn>
</template>

 <script setup lang="ts">
    import { useTheme } from "vuetify/lib/framework.mjs";
    import themeIcon from "vue-material-design-icons/ThemeLightDark.vue";

    var darkmode: boolean = true;

    const theme = useTheme()

    function switchTheme() {
        theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
    }
</script>

<style>
.theme-icon {
  margin-top: auto;
  margin-bottom: 7px;
  transform: scale(2);
  position: bottom !important;
}
</style>
