import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * Oyun dünyasındaki bir odayı temsil eden kodlar.
 * Oda içeriğini(NPC,eşyalar) tutar.
 */
public class Room {
    private final int id;
    private final String name;
    private final String description;
    // Çıkışlar (kuzey, guney, ...)
    private final Map<String, Room> exits;
    // Kilitli Çıkışlar ("kirmizi_kapi")
    private final Map<String, String> lockedExits;
    private final List<Item> items;
    private final List<Npc1> npcs;

    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.lockedExits = new HashMap<>();
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }
    // Dışarıdan erişim için yazılan metotlar.
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public Map<String, Room> getExits() {
        return exits;
    }

    public Map<String, String> getLockedExits() {
        return lockedExits;
    }
    
    public List<Item> getItems() {
        return items;
    }

    public List<Npc1> getNpcs() {
        return npcs;
    }

    // İki oda arasında bağlantı kurulumu.

    public void connect(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }
    
    // İki oda arasında kilitli bağlantı kurulumu.
    
    public void connectLocked(String direction, Room neighbor, String lockId) {
        exits.put(direction, neighbor);
        lockedExits.put(direction, lockId);
    }

    // Belirtilen yöndeki çıkış döndürülür.
    
    public Room getExit(String direction) {
        return exits.get(direction);
    }
    
    // Oda içeriği ekrana yazdılır.
    public void describe() {
        System.out.println("\n*** Şu anda bulunduğun yer: " + name + " ***");
        System.out.println(description);
        
        // Çıkışlar listelenir.
        System.out.print("Çıkışlar: ");
        if (exits.isEmpty()) {
            System.out.println("Hiç çıkış yok.");
        } else {
            List<String> exitDescriptions = new ArrayList<>();
            for (String direction : exits.keySet()) {
                String desc = direction;
                if (lockedExits.containsKey(direction)) {
                    desc += " (KİLİTLİ - " + lockedExits.get(direction) + ")";
                }
                exitDescriptions.add(desc);
            }
            System.out.println(String.join(", ", exitDescriptions));
        }

        // Eşyalar listelenir.
        System.out.print("Eşyalar: ");
        if (items.isEmpty()) {
            System.out.println("Hiç eşya yok.");
        } else {
            List<String> itemNames = new ArrayList<>();
            for (Item item : items) {
                itemNames.add(item.getName());
            }
            System.out.println(String.join(", ", itemNames));
        }

        // Karakterler listelenir.
        System.out.print("Karakterler: ");
        if (npcs.isEmpty()) {
            System.out.println("Hiç karakter yok.");
        } else {
            List<String> npcNames = new ArrayList<>();
            for (Npc1 npc : npcs) {
                npcNames.add(npc.getShortName());
            }
            System.out.println(String.join(", ", npcNames));
        }
        System.out.println("----------------------------------------");
    }
    
    // İsimle eşya aranması.

    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
    
    // İsimle NPC aranması.
    
public Npc1 getNPC(String shortName) {
        for (Npc1 npc : npcs) {
            if (npc.getShortName().equals(shortName)) {
                return npc;
            }
        }
        return null;
    }
}
// Tuzak odası. İçeri girildiğinde oyun biter.

class TrapRoom extends Room {
    
    public TrapRoom(int id, String name, String description) {
        super(id, name, description);
    }

    // Odaya girildiğinde tetiklenen metot.

    public void onEnter(Player p, GameEngine ctx) {

        System.out.println("\n##########################################");
        System.out.println("Odaya girdiğin anda soğuk bir rüzgarı hissettin.");
        System.out.println("Sen ne olduğunu anlayamadan 3 metrelik bir trol seni gövdenden tutup havaya kaldırdı.");
        System.out.println("Korkudan donakaldın...");
        System.out.println("Seni kokladı ve sen karşı koyamadan kafanı gövdenle birlikte ısırdı.");
        System.out.println("!!! MACERAN HAZİN BİR SON BULDU !!!");
        System.out.println("##########################################\n");
        
        // Oyunu bitmesi için can sıfırlanır.
        p.setHp(0); 
    }
}