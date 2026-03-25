import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


// Oyunun ana akışının, komut işlemeyi ve dünya yapısının (Map) yönetilmesi.
public class GameEngine {
    private Player player;
    private final Map<Integer, Room> map; 
    private boolean isRunning;
    private final Map<String, Boolean> lockStates; 

    public GameEngine() {
        this.map = new HashMap<>(); 
        this.lockStates = new HashMap<>();
        this.isRunning = true;
    }
    
    // Oyunun başlatması. Odaların, eşyaların, NPC'lerin oluşturulması ve bağlantıların kurulması.
     
    public void initGame() {
        System.out.println("Mini Macera Oyunu Başlatılıyor...");
        
        // Odaları oluşturulması
        Room salon = new Room(1, "Salon", "Büyük bir salon. Köşede yaşlı bir adam oturuyor. Duvarlarda eski tablolar asılı.");
        Room koridor = new Room(2, "Koridor", "Dar ve uzun bir koridor. Batı ucu karanlık bir kapıya açılıyor.");
        Room mutfak = new Room(3, "Mutfak", "Kullanılmayan, tozlu bir mutfak. Tezgâhın üzerinde parlayan bir şey var.");
        Room kütüphane = new Room(4, "Kütüphane", "Tavanlara kadar uzanan kitap raflarıyla dolu sessiz bir kütüphane.");
        Room mahzen = new Room(5, "Mahzen", "Rutubetli, soğuk bir mahzen. Burada bir muhafız nöbet tutuyor.");
        Room gizliOda = new Room(6, "Gizli Oda", "Başarıyla ulaştın! Burası maceranın sonu. İçeride büyük bir hazine var."); 
        TrapRoom tuzakOdasi = new TrapRoom(7, "Kanlık Oda", "");
        
        // Haritaya odaları ID'leri ile eklemenmesi
        map.put(salon.getId(), salon);
        map.put(koridor.getId(), koridor);
        map.put(mutfak.getId(), mutfak);
        map.put(kütüphane.getId(), kütüphane);
        map.put(mahzen.getId(), mahzen);
        map.put(gizliOda.getId(), gizliOda);
        map.put(tuzakOdasi.getId(), tuzakOdasi);
        
        // Odalar arası bağlantıların kurulması
        salon.connect("kuzey", koridor);
        salon.connect("bati", mutfak);

        koridor.connect("guney", salon);
        koridor.connect("dogu", kütüphane);
        koridor.connectLocked("bati", mahzen, "kirmizi_anahtar_kilidi"); 
        lockStates.put("kirmizi_anahtar_kilidi", true); 

        mutfak.connect("dogu", salon);
        mutfak.connect("kuzey", tuzakOdasi);

        kütüphane.connect("bati", koridor);
        kütüphane.connect("kuzey", gizliOda); 

        mahzen.connect("dogu", koridor);
        
        // Itemlerin oluşturulması
        Item key = new KeyItem("item_key_1", "kirmizi_anahtar", "Üzerinde tuhaf semboller olan paslı bir anahtar.", "kirmizi_anahtar_kilidi");
        Item potion = new PotionItem("item_potion_1", "saglik_iksiri", "Canını 15 puan artırır.", 15);
        Item sword = new WeaponItem("item_weapon_1", "eski_kilic", "Paslı ama güçlü bir kılıç. Saldırı gücünü 10 artırır.", 10); 
        
        // Odalara itemlerin eklenmesi
        mutfak.getItems().add(key); 
        kütüphane.getItems().add(potion); 
        mahzen.getItems().add(sword); 
        
        // NPC'lerin oluşturulması
        Npc1 wiseMan = FriendlyNPC.createWiseMan();
        Npc1 guard = EnemyNPC.createGuard();
        
        // NPC'lerin odalara yerleştirilmesi
        salon.getNpcs().add(wiseMan); 
        mahzen.getNpcs().add(guard); 
        
        // Oyuncunun oluşturulması
        player = new Player(salon, 100, 1);
        System.out.println("Oyun hazır. Başarılar dileriz!\n");
        player.getCurrentRoom().describe();
    }
    
    // Belirtilen kilit ID'sine sahip çıkışın kilidinin açılması.

    public boolean unlockExit(String lockId) {
        if (lockStates.containsKey(lockId) && lockStates.get(lockId)) {
            lockStates.put(lockId, false);
            return true;
        }
        return false;
    }

    // Ana oyun döngüsünün çalıştırılması. 
    public void startGameLoop() {
        Scanner scanner = new Scanner(System.in);
        printHelp();
        
        while (isRunning) {
            System.out.print("\n> ");
            try { // Sistemsel istisnalar
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.isEmpty()) continue;
                
                processCommand(input);
                
                // Kazanma koşulu kontrolü
                if (player.getCurrentRoom().getName().equals("Gizli Oda")) {
                    System.out.println("\n*** TEBRİKLER! ***");
                    System.out.println("Gizli Odaya ulaştın ve büyük hazineyi buldun!");
                    System.out.println("Maceran sona erdi.");
                    isRunning = false;
                }
                
                // Oyuncu Can kontrolü
                if (player.getHp() <= 0) {
                     System.out.println("\n*** OYUN BİTTİ ***");
                     // Özel tuzak odası mesajı
                     if (!player.getCurrentRoom().getName().equals("Tuzak Odası")) {
                         System.out.println("Can puanın sıfırlandı. Maalesef maceran burada sona erdi.");
                     }
                     isRunning = false;
                }
                
            } catch (Exception e) {
                System.out.println("!!! HATA: Beklenmedik bir sorun oluştu. Detay: " + e.getMessage()); 
            } 
        }
        // Oyun sonu özeti
        System.out.println("\n=== OYUN ÖZETİ ===");
        System.out.println("Can Puanın: " + player.getHp());
        System.out.println("Saldırı Gücün: " + player.getAttackPower());
        System.out.println("Son konumun: " + player.getCurrentRoom().getName());
        scanner.close();
    }

    // Kullanıcıdan alınan komutun işlenmesi ve ilgili sınıfların tetiklenmesi.

    private void processCommand(String commandText) {
        String[] parts = commandText.split(" ");
        String command = parts[0];
        // Sadece ikinci kelimenin yakalanması
        String argument = parts.length > 1 ? parts[1] : ""; 
        
        Room currentRoom = player.getCurrentRoom();

        switch (command) {
            case "look":
                currentRoom.describe();
                break;
                
            case "go":
                if (argument.isEmpty()) {
                    System.out.println(">>> Gitmek istediğin yönü belirtmelisin. Örnek: go kuzey");
                    return;
                }
                
                // Kilitli mi diye kontrol edilmesi
                if (currentRoom.getLockedExits().containsKey(argument) && lockStates.getOrDefault(currentRoom.getLockedExits().get(argument), false)) {
                    System.out.println(">>> Bu yöndeki (" + argument + ") geçit kilitli.");
                    return;
                }
                
                Room nextRoom = currentRoom.getExit(argument);
                if (nextRoom != null) {
                    player.move(argument);
                    
                    // Odaya girildikten sonra kontrol
                    Room enteredRoom = player.getCurrentRoom();
                    
                    if (enteredRoom instanceof TrapRoom) {
                        // Odaya girme olayının tetiklenmesi ve oyunun bitirilmesi
                        ((TrapRoom) enteredRoom).onEnter(player, this);
                    }
                    
                    // Oda açıklamasının yazdırılması
                    enteredRoom.describe(); 
                } else {
                    System.out.println(">>> Bu yönde çıkış yok.");
                }
                break;
                
            case "take":
                if (argument.isEmpty()) {
                    System.out.println(">>> Almak istediğin eşyayı belirtmelisin. Örnek: take anahtar");
                    return;
                }
                player.take(argument);
                break;
                
            case "use":
                if (argument.isEmpty()) {
                    System.out.println(">>> Kullanmak istediğin eşyayı belirtmelisin. Örnek: use iksir");
                    return;
                }
                // KeyItem kullanımında kilit açma ve geçiş GameEngine tarafından sağlanır.
                player.use(argument, this); 
                break;
                
            case "talk":
                if (argument.isEmpty()) {
                    System.out.println(">>> Konuşmak istediğin karakteri belirtmelisin. Örnek: talk muhafiz");
                    return;
                }
                player.talkToNPC(argument, this);
                break;
            
            case "say": 
                String sayText = commandText.substring(command.length()).trim();
                
                if (sayText.isEmpty()) {
                    System.out.println(">>> Ne söylemek istediğini belirtmelisin. Örnek: say merhaba dünya");
                    return;
                }
                
                System.out.println(">>> Şunu söylüyorsun: \"" + sayText + "\"");
                
                break;
            
            case "inv":
                player.inventoryText();
                break;
                
            case "help":
                printHelp();
                break;
                
            case "quit":
                isRunning = false;
                System.out.println(">>> Oyundan çıkılıyor...");
                break;
                
            default:
                System.out.println(">>> Bilinmeyen komut. 'help' yazarak komutları görebilirsin.");
                break;
        }
    }
    
    // Kullanılabilecek komutların ekrana yazdırılması.
    
    private void printHelp() {
        System.out.println("\nKullanılabilir Komutlar:");
        System.out.println("----------------------------------------");
        System.out.println("  look            : Odanın detaylarını (eşya, NPC, çıkışlar) tekrar listeler.");
        System.out.println("  go (yön)        : Belirtilen yöne gider (kuzey, guney, bati, dogu).");
        System.out.println("  take (eşya)     : Odadaki bir eşyayı envantere alır.");
        System.out.println("  use (eşya)      : Envanterdeki bir eşyayı kullanır.");
        System.out.println("  talk (karakter) : Odadaki bir NPC ile konuşur.");
        System.out.println("  say (metin)     : Ortama bir şey söylersin."); 
        System.out.println("  inv             : Envanterini ve can puanını gösterir.");
        System.out.println("  help            : Bu yardım menüsünü gösterir.");
        System.out.println("  quit            : Oyunu sonlandırır.");
        System.out.println("----------------------------------------");
    }
}