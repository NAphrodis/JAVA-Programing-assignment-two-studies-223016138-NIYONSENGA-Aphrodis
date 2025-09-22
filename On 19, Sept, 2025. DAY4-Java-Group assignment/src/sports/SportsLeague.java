package sports;

class Player {
    private String name;
    private int score;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}

class Team {
    private String teamName;
    private Player[] players;

 
    public Team(String teamName, Player[] players) {
        this.teamName = teamName;
        this.players = players;
    }

   
    public int getTotalScore() {
        int total = 0;
        for (Player p : players) {
            total += p.getScore();
        }
        return total;
    }

    public Player getHighestScoringPlayer() {
        Player top = players[0];
        for (Player p : players) {
            if (p.getScore() > top.getScore()) {
                top = p;
            }
        }
        return top;
    }

    public String getTeamName() {
        return teamName;
    }
}

public class SportsLeague {
    public static void main(String[] args) {
   
        Player[] teamAPlayers = {
            new Player("Aphrozic", 30),
            new Player("Emizic", 25),
            new Player("Messi", 40),
            new Player("Valensi", 12)
        };

    
        Player[] teamBPlayers = {
            new Player("Ronaldo", 20),
            new Player("Ractic", 35),
            new Player("Gatete", 50),
            new Player("Okecho", 32)
        };

     
        Team teamA = new Team("Team APR", teamAPlayers);
        Team teamB = new Team("Team Rayon rwanda", teamBPlayers);

        
        System.out.println(". " + teamA.getTeamName() + " Total Score: " + teamA.getTotalScore());
        System.out.println("Top Player: " + teamA.getHighestScoringPlayer().getName());

        System.out.println(". " + teamB.getTeamName() + " Total Score: " + teamB.getTotalScore());
        System.out.println("Top Player: " + teamB.getHighestScoringPlayer().getName());

     
        Team winner = (teamA.getTotalScore() > teamB.getTotalScore()) ? teamA : teamB;

        System.out.println("\n The team with the highest score is: " + winner.getTeamName() +
                           " (Total: " + winner.getTotalScore() + ")");
    }
}
