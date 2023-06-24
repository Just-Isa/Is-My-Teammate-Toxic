import { IGetToxicitiyDTO } from "@/domain/ICalculation";
import { computed, reactive, readonly, ref, watch } from "vue";

const toxicityState = reactive<IGetToxicitiyDTO>({
    toxicityLevel: 0,
    toxicityValues: []
});