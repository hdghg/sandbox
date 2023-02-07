#include <stdio.h>

void insertionSort(int arr[], int size) {
    int i, buf, j;

    for (i = 1; i < size; i++) {
        j = i;
        buf = arr[i];
        while (j > 0 && arr[j - 1] > buf) {
            arr[j] = arr[j - 1];
            j--;
        }
        arr[j] = buf;
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
    insertionSort(arr, size);
    for (i = 0; i < size; i++) {
        printf("%d\n", arr[i]);
    }

    return 0;
}
