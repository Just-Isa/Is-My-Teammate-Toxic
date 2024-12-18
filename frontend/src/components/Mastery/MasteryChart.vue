<template>
    <div class="chart-container">
      <div id="chart-div">
        <GChart
          :type="props.type"
          :data="chartData"
          :options="props.type === 'BarChart' ? chartBarOptions : (props.type === 'ColumnChart' ? chartColumnOptions : chartPieOptions)"
        />
      </div>
    </div>
  </template>

<script setup lang="ts">
    import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
    import { useLolChampsService } from '@/services/LolChampService';
    import { GChart } from 'vue-google-charts'
    import { GoogleChartOptions } from 'vue-google-charts/dist/types';
    import { useTheme } from 'vuetify/lib/framework.mjs';

    const lolChampsService = useLolChampsService();
    const playerMasteryService = usePlayerMasteryService();
    const themeService = useTheme();

    // TODO: Make the charts be created according to type
    const props = defineProps<{
        type: 'ColumnChart' | 'PieChart' | 'BarChart';
    }>();

    // TODO: Text styling when swapping theme doesnt swap
    const chartBarOptions: GoogleChartOptions = {
        width: 1920,
        height: 700,
        colors:  [
            "#683cb4"
        ],
        chartArea: {
            width: '70%', height: '90%'
        },
        backgroundColor: 'transparent',
        hAxis: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
        },
        vAxis : {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        },
        legend: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        }
    };

    const chartColumnOptions: GoogleChartOptions = {
        width: 1920,
        height: 800,
        chartArea: {
            width: '60%', height: '90%'
        },
        colors:  [
            "#683cb4"
        ],
        backgroundColor: 'transparent',
        hAxis: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
            slantedText: true,
            slantedTextAngle: 80
        },
        vAxis : {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },

        },
        legend: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        }
    };

    const chartPieOptions: GoogleChartOptions = {
        width: 1920,
        height: 800,
        chartArea: {
            width: '90%', height: '90%'
        },
        colors:  [
            "#683cb4",
            "#FF5733",
            "#33FF57",
            "#FF33FF",
            "#33A1FF",
            "#FF9F33",
            "#8A2BE2",
            "#FFD700",
            "#00CED1",
            "#FF6347",
            "#32CD32",
            "#00FF00",
            "#8B4513",
            "#D2691E",
            "#FF1493",
            "#9932CC",
            "#FF8C00",
            "#3CB371",
            "#ADFF2F",
            "#FF4500",
            "#FFD700",
            "#FF1493",
            "#48D1CC",
            "#FF00FF",
            "#800080",
            "#FF6347",
            "#20B2AA",
            "#B0C4DE",
            "#7FFF00",
            "#FFFF00",
            "#0000FF",
            "#FF0000",
            "#00BFFF",
            "#FF7F50",
            "#800000",
            "#008080",
            "#FFB6C1",
            "#20B2AA",
            "#F4A460",
            "#DC143C"
        ],
        scrollArrows:{
            activeColor: '#683cb4',
            inactiveColor:'#e0e0e0'
        },
        pieHole: 0.2,
        backgroundColor: 'transparent',
        hAxis: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
        },
        vAxis : {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        },
        legend: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        },
        slices: {
            0: { offset: 0.09 },
            1: { offset: 0.08 },
            2: { offset: 0.06 },
            3: { offset: 0.04 },
            4: { offset: 0.02 },
            5: { offset: 0.01 }
        },
    };


    const topMasteries = playerMasteryService.playerMasteryState.playerMastery.slice(0, 50);

    const chartData = [
        ['Champion', 'Points'],
    ]

    topMasteries.forEach((m) => {
        const championPoints = Number(m.championPoints);
        const championName = String(lolChampsService.lolChampState.data[m.championId] as unknown as string);

        chartData.push([championName, championPoints])
    })
</script>

<style>

</style>
