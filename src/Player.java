import java.util.ArrayList;
import java.util.List;

// Oyuncunun durumunu (HP, envanter, mevcut oda, saldırı gücü) ve etkileşimlerini yönetir.
 
public class Player {
    private Room currentRoom;
    private final List<Item> inventory;
    private int hp;
    private int attackPower;

    public Player(Room startRoom, int initialHp, int initialAttack) {
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.hp = initialHp;
        this.attackPower = initialAttack;
    }
    // Dışarıdan erişim için yazılan metotlar.
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public int getAttackPower() {
        return attackPower;
    }
    
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    // Oyuncunun belirtilen yöne hareket etmesi sağlanır.

    public Room move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println(">>> Bu yönde çıkış yok.");
            return null;
        }

        currentRoom = nextRoom;
        return currentRoom;
    }

    // Odadaki eşyanın envantere alınabilmesi.
 
    public boolean take(String itemName) {
        Item item = currentRoom.getItem(itemName);
        if (item == null) {
            System.out.println(">>> Odada böyle bir eşya (" + itemName + ") bulunmuyor.");
            return false;
        }

        currentRoom.getItems().remove(item);
        inventory.add(item);
        System.out.println(">>> " + item.getName() + " adlı eşyayı aldın.");
        return true;
    }
    
    // Envanterdeki eşyanın bulunması.

    public Item findItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    // Envanterdeki eşyanın kullanılması.

    public boolean use(String itemName, GameEngine ctx) {
        Item item = findItemInInventory(itemName);
        if (item == null) {
            System.out.println(">>> Bu eşya (" + itemName + ") sende yok.");
            return false;
        }

        item.onUse(this, ctx);
        return true;
    }

    // Odadaki NPC ile konuşma.

    public boolean talkToNPC(String npcName, GameEngine ctx) {
        Npc1 npc = currentRoom.getNPC(npcName);
        if (npc == null) {
            System.out.println(">>> Odada böyle bir karakter (" + npcName + ") bulunmuyor.");
            return false;
        }
        
        npc.talk(this, ctx);
        return true;
    }

    // Oyuncu envanterinin ve mevcut durumunun listelenmesi.
    
    public void inventoryText() {
        System.out.println("\n--- ENVANTER ---");
        System.out.println("Can Puanın (HP): " + hp);
        System.out.println("Saldırı Gücün (ATK): " + attackPower);
        
        if (inventory.isEmpty()) {
            System.out.println("Envanterin boş.");
        } else {
            System.out.println("Taşıdığın Eşyalar:");
            for (Item item : inventory) {
                System.out.println(" - " + item.getName() );
            }
        }
        System.out.println("------------------\n");
    }
}