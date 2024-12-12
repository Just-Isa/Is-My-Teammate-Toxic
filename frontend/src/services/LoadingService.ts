import { reactive, readonly, ref } from 'vue';

interface ILoading {
    isLoading: boolean
}

const loadingState = reactive<ILoading>({
    isLoading: false
});

function loadingFinished(): void {
    loadingState.isLoading = false;
}

function loadingStarted(): void {
    loadingState.isLoading = true;
}

export function useLoadingService() {
    return {
        loadingState: readonly(loadingState),
        loadingFinished,
        loadingStarted
    }
}
