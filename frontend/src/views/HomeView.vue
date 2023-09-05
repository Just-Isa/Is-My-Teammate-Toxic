<template>
  <h1>HOME</h1>

  <v-card width="fit-content" variant="plain" class="quote">
    <template v-slot:title>
      The struggle itself towards the heights is enough to fill a man's heart. One must imagine Sisyphus happy.
    </template>

    <template v-slot:text>
      Yes, this even applies for League of Legends, mom.
    </template>
  </v-card>
  <div class="sisyphus">
    <img src="../assets/sisyphus.svg">
  </div>
  <Navigation></Navigation>
  <Footer />
</template>

<script setup lang="ts">
import Navigation from "@/components/Navigation.vue";
import Footer from "@/components/Footer.vue";
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.sisyphus {
  filter: invert(45%) sepia(70%) saturate(926%) hue-rotate(304deg) brightness(93%) contrast(94%);
  z-index: -20;
  transform: scaleX(-1);
}

.quote {
  filter: invert(45%) sepia(70%) saturate(926%) hue-rotate(304deg) brightness(93%) contrast(94%);
  margin-left: auto;
  margin-right: auto;
  text-align: center;
}
</style>
