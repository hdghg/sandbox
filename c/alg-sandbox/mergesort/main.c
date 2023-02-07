#include <stdio.h>
#include <limits.h>
#include <stdlib.h>

/**
 *
 * @param arr Array to sort
 * @param p index of left side subarray
 * @param q index of right subarray
 * @param r index of end of right subarray plus 1
 */
void merge(int arr[], int p, int q, int r) {
    int *left, *right, i, j = 0, k = 0;
    left = malloc(sizeof(int) * (q - p + 1));
    right = malloc(sizeof(int) * (r - q + 1));

    for (i = p; i < q; i++) {
        left[i - p] = arr[i];
    }
    left[i - p] = INT_MAX;
    for (i = q; i < r; i++) {
        right[i - q] = arr[i];
    }
    right[i - q] = INT_MAX;
    for (i = p; i < r; i++) {
        if (left[j] < right[k]) {
            arr[i] = left[j++];
        } else {
            arr[i] = right[k++];
        }
    }
    free(left);
    free(right);
}

void mergeSort(int arr[], int start, int end) {
    int q;
    if (start < end - 1) {
        q = (start + end) / 2;
        mergeSort(arr, start, q);
        mergeSort(arr, q, end);
        merge(arr, start, q, end);
    }
}

int main() {
    int size, i;
    int arr[1000];
    scanf("%d", &size);
    if (size > 1000 || size < 0) {
        return -1;
    }
    for (i = 0; i < size; i++) {
        scanf("%d", &arr[i]);
    }
    mergeSort(arr, 0, size);
    for (i = 0; i < size; i++) {
        printf("%d\n", arr[i]);
    }
}
