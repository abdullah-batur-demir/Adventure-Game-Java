
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Oyundaki NPC'lerin temel özelliklerini tanımlar.

public abstract class Npc1{
    protected String name;
    protected String description;

    public Npc1(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // Dışarıdan erişim için yazılan metotlar.
    public String getName() {
        return name;
    }

        public String getShortName() {
        return name.toLowerCase().replace(" ", "_").replaceAll("[ıİ]", "i").replaceAll("[üÜ]", "u").replaceAll("[öÖ]", "o").replaceAll("[çÇ]", "c").replaceAll("[ğĞ]", "g").replaceAll("[şŞ]", "s");
    }
    public String getDescription() {
        return description;
    }

    // Oyuncu bu karakterle konuşmaya çalıştığında ne olacağını belirler.
    public abstract void talk(Player p, GameEngine ctx);
}

// NPC sınıfından türetilmiş Dost Karakter sınıfı.

class FriendlyNPC extends Npc1 {
    private final ConversationNode dialogueRoot;

    public FriendlyNPC(String name, String description, ConversationNode dialogueRoot) {
        super(name, description);
        this.dialogueRoot = dialogueRoot;
    }

    // Dost karakterle konuşulduğunda diyalog ağacında ilerlenir.
    
    @Override
    public void talk(Player p, GameEngine ctx) {
        System.out.println(">>> " + this.name + " ile konuşuyorsun.");
        
        ConversationNode currentNode = dialogueRoot;
        Scanner scanner = new Scanner(System.in);
        
        while (currentNode != null && !currentNode.getChoices().isEmpty()) {
            currentNode.display();
            
            // Oyuncunun seçenek numarası girerek diyaloga yön vermesi.
            System.out.print("Seçimini yap (1-" + currentNode.getChoices().size() + ") veya (cikis): ");
            String input = scanner.nextLine().trim().toLowerCase();
            scanner.close();
            if (input.equals("cikis")) {
                System.out.println(">>> Konuşmayı sonlandırdın.");
                return;
            }
            
            try {
                int choiceIndex = Integer.parseInt(input) - 1;
                List<String> choices = currentNode.getChoices();
                
                if (choiceIndex >= 0 && choiceIndex < choices.size()) {
                    String selectedChoiceText = choices.get(choiceIndex);
                    // Seçilen seçeneğe karşılık gelen sonraki düğümün alınması.
                    currentNode = currentNode.getNextNode(selectedChoiceText);
                    
                    if (currentNode == null) {
                        System.out.println(">>> " + this.name + ": Anladım. Bu konuda söyleyeceklerim bu kadardı.");
                        break;
                    }
                    
                } else {
                    System.out.println(">>> Geçersiz seçim. Lütfen geçerli bir numara gir.");
                }
            } catch (NumberFormatException e) {
                System.out.println(">>> Geçersiz komut. Lütfen bir numara veya 'cikis' yazın.");
            }
        }
        
        if (currentNode != null) {
            currentNode.display();
        }
    }
    
    // Dost NPC oluşturulması. İki adımlı bir diyalog ağacı kurulur.

    public static FriendlyNPC createWiseMan() {
        
        ConversationNode nodeGoodbye = new ConversationNode(
            "Görüşmek üzere gezgin. Umarım aradığını bulursun.", 
            null
        );
        
        ConversationNode nodeKeyInfo = new ConversationNode(
            "Anahtar, Salon'un hemen batısındaki Mutfak'ta saklıdır. Dikkatli ol, etrafta tuzaklar olabilir.", 
            Map.of("Teşekkürler, gidiyorum.", nodeGoodbye) 
        );
        
        ConversationNode nodeGateInfo = new ConversationNode(
            "Karanlık Kapı, Koridor'un batı ucundaki kilitli geçittir. Oraya ulaşmak için kilitli anahtarı bulmalısın.", 
            Map.of("Peki, bana şans dile.", nodeGoodbye) 
        );
        
        Map<String, ConversationNode> rootChoices = new HashMap<>();
        rootChoices.put("Anahtar nerede?", nodeKeyInfo);
        rootChoices.put("Karanlık Kapı da ne?", nodeGateInfo);
        rootChoices.put("Hoşça kal.", nodeGoodbye);
        
        ConversationNode nodeRoot = new ConversationNode(
            "Selam gezgin. Bu diyarları görmeyeli uzun zaman oldu. Karanlık Kapı'dan geçmek istiyorsan, kırmızı anahtarı bulmalısın.",
            rootChoices
        );
        
        return new FriendlyNPC("Bilge Adam", "Odanın köşesinde oturmuş yaşlı ve bilge görünümlü bir adam.", nodeRoot);
       
    }

}
// NPC sınıfından türetilmiş Düşman Karakter sınıfı.


 class EnemyNPC extends Npc1 {
    private final int attackPower;

    public EnemyNPC(String name, String description, int attackPower) {
        super(name, description);
        this.attackPower = attackPower;
    }

    // Düşman karakterle konuşulduğunda tehdit eder ve hemen ardından saldırır.

    @Override
    public void talk(Player p, GameEngine ctx) {
        System.out.println(">>> " + this.name + " sana hırlıyor ve kılıcını çekiyor!");
        System.out.println(">>> \"Git buradan! Yoksa...\" der ve anında saldırır!");
        
        attack(p);
    }
    
    // Düşmanın oyuncuya saldırma metotu.

    public void attack(Player p) {
        int damage = this.attackPower;
        
        p.setHp(p.getHp() - damage);
        
        System.out.println("<<< Düşman (" + this.name + ") sana " + damage + " hasar verdi.");
        System.out.println("<<< Kalan Can Puanın: " + p.getHp());

        if (p.getHp() <= 0) {
            System.out.println("!!! Düşman seni mağlup etti. Canın kalmadı.");
        }
    }
    
    // Düşman NPC oluşturulması.
    
    public static EnemyNPC createGuard() {
        return new EnemyNPC("muhafız", "Ağır zırh giymiş, kılıçlı, gözleri tehditkar bir muhafız.", 10);
    }
}