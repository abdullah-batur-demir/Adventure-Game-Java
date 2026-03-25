import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * NPC diyalog ağacındaki bir düğümü temsil eder.
 * Bir metin ve sonraki olası diyalog düğümlerine (seçenekler) geçişi tutar.
 */
class ConversationNode {
    private final String text;
    private final Map<String, ConversationNode> nextNodes; 

    public ConversationNode(String text, Map<String, ConversationNode> nextNodes) {
        this.text = text;
        this.nextNodes = nextNodes != null ? nextNodes : Collections.emptyMap();
    }

    public String getText() {
        return text;
    }

    // Oyuncunun görebileceği seçenek metinlerinin döndürülmesi.
    public List<String> getChoices() {
        return nextNodes.keySet().stream().collect(Collectors.toList());
    }
    
    // Belirtilen seçenek metnine karşılık gelen bir sonraki düğümün döndürülmesi.

    public ConversationNode getNextNode(String choiceText) {
        return nextNodes.get(choiceText);
    }
    
    // Diyalog düğümünün ekrana yazdırılması.
    
    public void display() {
        System.out.println("\n--- Diyalog ---");
        System.out.println(text);
        
        List<String> choices = getChoices();
        if (!choices.isEmpty()) {
            System.out.println("\nSeçenekler:");
            // Seçenekleri numaralandırarak gösteririz
            for (int i = 0; i < choices.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + choices.get(i));
            }
        } else {
            System.out.println("(Diyalog sona erdi.)");
        }
        System.out.println("---------------\n");
    }
}