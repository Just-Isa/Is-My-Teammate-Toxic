<template>
  <home-button/>
  <Navigation class="navbar"></Navigation>
  <div class="container">
      <div v-for="source in sources" class="type-container">
          <v-card
          class="card"
          max-width="400"
          min-width="400"
          >

              <v-card-title class="card-title">
              {{ source.name }} <br>

              <b>{{ source.link }} </b>

              </v-card-title>

              <v-card-actions>
              <v-btn
                  color="blue-lighten-2"
                  variant="elevated"
                  @click="goToLink(source.link)"
                  size="x-large"
                  class="card-button"
              >
                  Check it out!
              </v-btn>

              </v-card-actions>
          </v-card>
      </div>
  </div>
</template>

<script setup lang="ts">
  import { onMounted } from 'vue';
  import sources from "@/assets/sources.json";
  import Navigation from "@/components/Navigation.vue";

  function goToLink(link: string) {
      console.log("TODO: IMPLEMENT NEXT STEP")
      location.href = link;
  }
</script>

<style>
.container {
  margin-top: 40px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  justify-content: space-around;
  align-items: center;
}

.card{
  flex:1;
}

.card-title {
  margin-bottom: 20px;
}

.card-button{
  margin:0 auto;
  display:block;
}
</style>
