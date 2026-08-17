package mobile.model;

public enum Theme {
    MYSTERY,
    CRIME,
    BIOGRAPHY,
    HISTORY;

    public String toString() {
        String[] words = this.name().toLowerCase().split("_");
        StringBuilder displayName = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            displayName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return displayName.toString().trim();
    }

    }
