import java.util.*;

public class proj {
    private static final int FIRST_MAX = 38;
    private static final int FIRST_TAKE = 6;
    private static final int SECOND_MAX = 8;
    private static final int SECOND_TAKE = 3;
    private static final int DEFAULT_TICKETS = 5;
    public static void main (String args[]){
        Scanner sc = new Scanner(System.in);

        System.out.print("要隨機產生幾張號碼?(預設 "+ DEFAULT_TICKETS + "): ");
        String nStr = sc.nextLine().trim();
        int ticketCount = nStr.isEmpty() ? DEFAULT_TICKETS : Math.max(1, parseIntSafe(nStr, DEFAULT_TICKETS));

        List<Ticket> tickets = new ArrayList<>();
        for(int i = 0;i < ticketCount;i++)
            tickets.add(generateTicket());

        System.out.println("\n---隨機選號---");
        printTickets(tickets);

        System.out.println("\n---Enter本期中獎號碼---");
        SortedSet<Integer> winFirst  = readNumbers(sc,"第一區", 1, FIRST_MAX, FIRST_TAKE);
        SortedSet<Integer> winSecond  = readNumbers(sc,"第二區", 1, SECOND_MAX, SECOND_TAKE);
    
        System.out.println("\n--- 對獎結果 ---");
        //System.out.println(prizeTableDescription());
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            int m1 = countMatch(t.firstArea, winFirst);
            int m2 = countMatch(t.secondArea, winSecond);
            String prize = prizeName(m1, m2);
            System.out.printf("(%d) 第一區%s | 第二區%s ➜ %s%n",
                    i + 1, t.firstArea, t.secondArea, prize);
        }
        sc.close();
    }

    private static class Ticket{
        SortedSet<Integer> firstArea;
        SortedSet<Integer> secondArea;
        Ticket(SortedSet<Integer> f, SortedSet<Integer> s){
            this.firstArea = f; 
            this.secondArea = s;
        }
    }

    private static Ticket generateTicket(){
        List<Integer> bag1 = new LinkedList<>();
        for(int i = 1;i <= FIRST_MAX;i++)
            bag1.add(i);
        Collections.shuffle(bag1);
        SortedSet<Integer> first = new TreeSet<>(bag1.subList(0, FIRST_TAKE));

        Set<Integer> s2 = new HashSet<>();
        Random r = new Random();
        while(s2.size() < SECOND_TAKE){
            int x = 1 + r.nextInt(SECOND_MAX);
            s2.add(x);
        }
        SortedSet<Integer> second = new TreeSet<>(s2);

        return new Ticket(first, second);
    }

    private static SortedSet<Integer> readNumbers(Scanner sc, String area, int min,int max, int need){
        while(true){
            System.out.printf("%s: Enter %d 個介於 %d~%d 的不重複整數: %n", area, need, min, max);
            String line = sc.nextLine().trim();
            String[] parts = line.split("\\s+");
            if(parts.length != need){
                System.out.println("數字不符\n");
                continue;
            }
            SortedSet<Integer> set = new TreeSet<>();
            boolean check = true;
            for(String p: parts){
                if(!p.matches("-?\\d+")){
                    check = false;
                    break;
                }
                int v = Integer.parseInt(p);
                if(v < min || v > max){
                    check = false;
                    break;
                }
                set.add(v);
            }
            if(!check || set.size() != need){
                System.out.println("含非整數、越界或重複值\n");
                continue;
            }
            return set;
        }
    }

    private static int countMatch(SortedSet<Integer> a, SortedSet<Integer> b){
        int c = 0;
        for(int x : a){
            if(b.contains(x)) 
                c++;
        }
        return c;
    }

    private static String prizeName(int mFirst,int mSecond){
        if (mFirst == 6 && mSecond >= 1) return "頭獎";
        if (mFirst == 6 && mSecond == 0) return "二獎";
        if (mFirst == 5 && mSecond >= 1) return "三獎";
        if (mFirst == 5 && mSecond == 0) return "四獎";
        if (mFirst == 4 && mSecond == 1) return "五獎";
        if (mFirst == 4 && mSecond == 0) return "六獎";
        if (mFirst == 3 && mSecond >= 1) return "七獎";
        if (mFirst == 2 && mSecond >= 1) return "八獎";
        if (mFirst == 3 && mSecond == 0) return "九獎";
        if (mFirst == 1 && mSecond >= 1) return "普獎";

        return String.format("未中獎");
    }

    private static int parseIntSafe(String s, int fallback) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return fallback; }
    }

    private static void printTickets(List<Ticket> tickets) {
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            System.out.printf("(%d) 第一區: %s   第二區: %s%n", i + 1, t.firstArea, t.secondArea);
        }
    }
}