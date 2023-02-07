#include <stdio.h>

void bubbleSort(int arr[], int size) {
    int i, j, buf;
    for (i = 0; i < size - 1; i++) {
        for (j = i; j >= 0; j--) {
            if (arr[j] > arr[j + 1]) {
                buf = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = buf;
            }
        }
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
    bubbleSort(arr, size);
    for (i = 0; i < size; i++) {
        printf("%d\n", arr[i]);
    }
}
