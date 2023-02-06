package app;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws Exception {
        Draw.initWindow();
        runSort();
    }

    public static void runSort() {
        // Numbers to sort
        int[] feed = { 295, 125, 10, 440, 435, 480, 185, 65, 5, 55, 210, 255, 285, 220, 250, 325, 145, 415, 25, 205,
                100, 135, 200, 360, 420, 130, 30, 80, 175, 495, 45, 160, 155, 345, 405, 265, 380, 470, 350, 20, 180,
                320, 315, 310, 460, 370, 230, 485, 490, 465, 385, 90, 375, 50, 430, 105, 110, 215, 445, 450, 170, 270,
                330, 355, 475, 365, 70, 425, 455, 225, 140, 235, 150, 395, 95, 300, 195, 390, 290, 35, 260, 240, 400,
                410, 340, 500, 40, 335, 85, 190, 75, 60, 245, 15, 115, 165, 120, 275, 305, 280 };
        Draw.updateBars(feed);

        // Run algorithm
        long startTime = System.nanoTime();
        quickSort(feed); // Change sort type
        long stopTime = System.nanoTime();
        double runTime = (stopTime - startTime) * 0.000001;
        String timeMsg = String.format("Execution time: %.2f ms\n", runTime);
        System.out.println(timeMsg);
    }

    // Update window on operation
    public static void microUpdate(int[] nums, int ms) {
        try {
            Draw.updateBars(nums);
            TimeUnit.MICROSECONDS.sleep(ms);
        }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    // Print array to console
    public static void printArray(int[] nums) {
        String print = "";
        for (int num : nums)
            print += num + " ";
        System.out.println(print + "\n");
    }

    // Swap two elements in an array by index
    public static int[] swap(int[] arr, int a, int b) {
        int hold = arr[a];
        arr[a] = arr[b];
        arr[b] = hold;
        return arr;
    }

    // Progressively swap two elements
    public static int[] bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i+1; j < nums.length; j++) {
                if (nums[i] > nums[j]) nums = swap(nums, i, j);
                microUpdate(nums, 1);
            }
        }
        return nums;
    }

    // Swap smallest element to front
    public static int[] selectionSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int minIndex = i;
            for (int j = i+1; j < nums.length; j++)
                if (nums[j] < nums[minIndex]) minIndex = j;
            if (minIndex != i) nums = swap(nums, i, minIndex);
            microUpdate(nums, 70000);
        }
        return nums;
    }

    // Progressively divide, sort, and merge
    public static int[] mergeSort(int[] nums) {
        if (nums.length <= 1) return nums;

        // Divide
        int mid = nums.length / 2;
        int[] left = mergeSort(Arrays.copyOfRange(nums, 0, mid));
        int[] right = mergeSort(Arrays.copyOfRange(nums, mid, nums.length));

        // Merge
        for (int l = 0, r = 0; l < left.length || r < right.length;) {
            if (r == right.length || l < left.length && left[l] < right[r]) {
                nums[l+r] = left[l];
                l++;
            }
            else {
                nums[l+r] = right[r];
                r++;
            }
        }

        return nums;
    }

    // Get pivot and swap values on either side
    public static int[] quickSort(int[] nums) {
        return quickSort(nums, 0, nums.length-1);
    }

    public static int[] quickSort(int[] nums, int low, int high) {
        if (low >= high) return nums;

        int pivot = nums[high];
        int l = low;
        int r = high;

        while (l <= r) {
            while (nums[l] < pivot) l++;
            while (nums[r] > pivot) r--;
            if (l <= r) {
                nums = swap(nums, l, r);
                l++;
                r--;
                microUpdate(nums, 30000);
            }
        }

        if (low < r) nums = quickSort(nums, low, r);
        if (l < high) nums = quickSort(nums, l, high);

        return nums;
    }

    // Count occurrences of each element and reorder
    public static int[] countingSort(int[] nums) {
        return countingSort(nums, 1, 1);
    }

    public static int[] countingSort(int[] nums, int div, int mod) {
        int min = 0;
        int max = 9;
        if (mod == 1) {
            min = Arrays.stream(nums).min().getAsInt();
            max = Arrays.stream(nums).max().getAsInt();
        }
        int[] count = new int[max-min+1];

        // Count digits
        for (int i = 0; i < nums.length; i++) {
            if (mod == 1) count[(nums[i])-min]++;
            else count[Math.abs((nums[i]/div%mod)-min)]++;
        }

        // Modify count to sum of preceding values
        for (int i = 1; i < count.length; i++)
            count[i] = count[i] + count[i-1];
        
        // Reorder elements
        int[] output = new int[nums.length];
        for (int i = nums.length-1; i >= 0; i--) {
            if (mod == 1) {
                output[count[(nums[i])-min]-1] = nums[i]-min;
                count[(nums[i])-min]--;
            }
            else {
                output[count[Math.abs((nums[i]/div%mod)-min)]-1] = nums[i]-min;
                count[Math.abs((nums[i]/div%mod)-min)]--;
            }
        }

        return output;
    }

    // Sort by digits
    public static int[] radixSort(int[] nums) {
        // Get max digits
        int maxNum = Arrays.stream(nums).max().getAsInt();
        int maxDigits = 0;
        while (maxNum > 0) {
            maxNum /= 10;
            maxDigits++;
        }

        // Counting sort by digit
        int[] tempNums = nums.clone();
        for (int i = 1; i < Math.pow(10, maxDigits); i *= 10)
            tempNums = countingSort(tempNums, i, 10);

        return tempNums;
    }
}
