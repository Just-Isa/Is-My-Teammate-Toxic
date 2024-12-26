<template>
    <div class="chart-container">
      <div id="chart-div">
        <GChart
            :key="props.type"
            :type="props.type"
            :data="chartData"
            :options="chartOptions"
        />
      </div>
    </div>
  </template>

<script setup lang="ts">
    import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
    import { useLolChampsService } from '@/services/LolChampService';
    import { GChart } from 'vue-google-charts'
    import { useTheme } from 'vuetify/lib/framework.mjs';
    import { computed } from 'vue';

    const lolChampsService = useLolChampsService();
    const playerMasteryService = usePlayerMasteryService();
    const themeService = useTheme();

    // TODO: Make the charts be created according to type
    const props = defineProps<{
        type: 'ColumnChart' | 'PieChart' | 'BarChart';
    }>();
    // TODO: Text styling when swapping theme doesnt swap
    const chartOptions = computed(() => {
        const baseOptions = {
            backgroundColor: 'transparent',
            colors: ["#683cb4"],
            legend: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
            },
            hAxis: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
            },
            vAxis: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
            },
        };

        if (props.type === 'BarChart') {
            return {
                ...baseOptions,
                width: 1920,
                height: 700,
                hAxis: {
                    textStyle: {
                        color: themeService.current.value.dark ? "white" : "black",
                        fontSize: 14,
                        slantedText: false,
                    },
                    format: "short"
                },
                vAxis: {
                    textStyle: {
                        color: themeService.current.value.dark ? "white" : "black",
                        fontSize: 11,
                    },
                },
                chartArea: { width: '70%', height: '90%' },
            };
        }

        if (props.type === 'ColumnChart') {
            return {
                ...baseOptions,
                width: 1920,
                height: 600,
                chartArea: { width: '70%', height: '65%' },
                hAxis: {
                    ...baseOptions.hAxis,
                    slantedText: true,
                    slantedTextAngle: 80,
                    textStyle: {
                        color: themeService.current.value.dark ? "white" : "black",
                    },
                },
                vAxis: {
                    format: "short",
                    textStyle: {
                        color: themeService.current.value.dark ? "white" : "black",
                    },
                },
            };
        }

        if (props.type === 'PieChart') {
            return {
                ...baseOptions,
                width: 1920,
                height: 800,
                chartArea: { width: '90%', height: '90%' },
                pieHole: 0.2,
                legend: {
                    textStyle: {
                        color: themeService.current.value.dark ? "white" : "black",
                    },
                    scrollArrows:{
                        activeColor: '#dcd9f2',
                        inactiveColor:'#363636'
                    }
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
                slices: {
                0: { offset: 0.09 },
                1: { offset: 0.08 },
                2: { offset: 0.06 },
                3: { offset: 0.04 },
                4: { offset: 0.02 },
                5: { offset: 0.01 },
                },
            };
        }

        return baseOptions;
    });

    console.log(chartOptions.value)


    const topMasteries = playerMasteryService.playerMasteryState.playerMastery.slice(0, 50);

    const chartData = computed(() => {
        const data = [['Champion', 'Points']];
        topMasteries.forEach((m) => {
            const championPoints = Number(m.championPoints);
            const championName = String(lolChampsService.lolChampState.data[m.championId] as unknown as string);
            data.push([championName, championPoints]);
        });
        return data;
    });


</script>

<style>

</style>
