import java.util.*;

public class CashFlowMinimizer {

    static class Transaction {
        int from;
        int to;
        int amount;

        Transaction(int from, int to, int amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Person " + from + " pays " + amount + " to Person " + to;
        }
    }

    // Function to minimize cash flow among friends
    public static void minimizeCashFlow(int[][] graph) {
        int n = graph.length;
        int[] netAmount = new int[n];

        // Calculate the net amount for each person
        for (int p = 0; p < n; p++) {
            for (int i = 0; i < n; i++) {
                netAmount[p] += (graph[i][p] - graph[p][i]);
            }
        }

        List<Transaction> transactions = minimizeTransactions(netAmount);

        // Print the transactions
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static List<Transaction> minimizeTransactions(int[] netAmount) {
        List<Transaction> transactions = new ArrayList<>();

        PriorityQueue<Integer> positiveHeap = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> negativeHeap = new PriorityQueue<>();

        // Add people with credit and debit to respective heaps
        for (int i = 0; i < netAmount.length; i++) {
            if (netAmount[i] > 0) {
                positiveHeap.add(i);
            } else if (netAmount[i] < 0) {
                negativeHeap.add(i);
            }
        }

        // Minimize transactions
        while (!positiveHeap.isEmpty() && !negativeHeap.isEmpty()) {
            int maxCreditPerson = positiveHeap.poll();
            int maxDebitPerson = negativeHeap.poll();

            int amount = Math.min(netAmount[maxCreditPerson], -netAmount[maxDebitPerson]);
            netAmount[maxCreditPerson] -= amount;
            netAmount[maxDebitPerson] += amount;

            transactions.add(new Transaction(maxDebitPerson, maxCreditPerson, amount));

            if (netAmount[maxCreditPerson] > 0) {
                positiveHeap.add(maxCreditPerson);
            }
            if (netAmount[maxDebitPerson] < 0) {
                negativeHeap.add(maxDebitPerson);
            }
        }

        return transactions;
    }

    public static void main(String[] args) {
        // Example graph
        int[][] graph = {
            {0, 1000, 2000},
            {0, 0, 5000},
            {0, 0, 0}
        };

        minimizeCashFlow(graph);
    }
}
