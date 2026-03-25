
// Oyundaki tüm eşyaların ortak özelliklerini tanımlayan soyut üst sınıf.

public abstract class Item {

    protected final String id; 
    protected String name;
    protected String description;

    public Item(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Dışarıdan erişim için yazılan metotlar.

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Eşya kullanıldığında ne yapacağını tanımlayan metot.
   
    public abstract void onUse(Player p, GameEngine ctx);
}

// Item sınıfından türetilmiş İksir sınıfı.
class PotionItem extends Item {
    private final int healthBoost;

    public PotionItem(String id, String name, String description, int healthBoost) {
        super(id, name, description);
        this.healthBoost = healthBoost;
    }

    // İksir kullanıldığında oyuncunun canı artar.
    
    @Override
    public void onUse(Player p, GameEngine ctx) {
        p.setHp(p.getHp() + healthBoost);
        System.out.println(">>> " + this.getName() + " kullandın. Can puanın " + healthBoost + " arttı. Yeni canın: " + p.getHp());
        // İksir kullanıldıktan sonra envanterden kaldırılması.
        p.getInventory().remove(this);
    }
}

// Item sınıfından türetilmiş Anahtar sınıfı.
 
class KeyItem extends Item {
    private final String targetDoor;

    public KeyItem(String id, String name, String description, String targetDoor) {
        super(id, name, description);
        this.targetDoor = targetDoor;
    }

    // Anahtar kullanıldığında kilitli kapı açılır.
   
    @Override
    public void onUse(Player p, GameEngine ctx) {
        // Hedef kapının kilidi açmaya çalışılır.
        if (ctx.unlockExit(targetDoor)) {
            System.out.println(">>> " + this.getName() + " ile kilitli bir geçidin kilidini açtın! Artık oradan 'go' komutunu kullanarak geçebilirsin.");
        } else {
            System.out.println(">>> " + this.getName() + " hiçbir kilitli kapıyı açmadı veya kilit zaten açıktı. Belki de doğru yerde değilsin.");
        }
    }
    
    public String getTargetDoor() {
        return targetDoor;
    }
}

// Item sınıfından türetilmiş Silah sınıfı.

class WeaponItem extends Item {
    private final int attackBoost;
    private boolean isEquipped; // Silahın kuşanılıp kuşanılmadığını kontrol etmek için.

    public WeaponItem(String id, String name, String description, int attackBoost) {
        super(id, name, description);
        this.attackBoost = attackBoost;
        this.isEquipped = false;
    }

    // Silah kullanıldığında(kuşanılır) Saldırı gücünü artırır.

    @Override
    public void onUse(Player p, GameEngine ctx) {
        if (isEquipped) {
            // Silahı çıkarma.
            p.setAttackPower(p.getAttackPower() - attackBoost);
            isEquipped = false;
            System.out.println(">>> " + this.getName() + "'ı kınına koydun. Mevcut saldırı gücün: " + p.getAttackPower());
        } else {
            // Silahı kuşanma.
            p.setAttackPower(p.getAttackPower() + attackBoost);
            isEquipped = true;
            System.out.println(">>> " + this.getName() + "'ı kınından çıkardın. Saldırı gücün " + attackBoost + " arttı. Mevcut saldırı gücün: " + p.getAttackPower());
        }
    }
    
    public boolean isEquipped() {
        return isEquipped;
    }
}