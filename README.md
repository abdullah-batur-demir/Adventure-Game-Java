# Java OOP: Metin Tabanlı Macera Oyunu (Text-Based Adventure)

Bu proje, Java kullanılarak geliştirilmiş, genişletilebilir bir oyun motoru mimarisine sahip **Metin Tabanlı bir RPG (Macera)** uygulamasıdır. Yazılım mimarisi, karmaşık nesne ilişkilerini ve çalışma zamanı davranışlarını yönetmek için gelişmiş OOP teknikleri üzerine kurulmuştur.

## 🏗️ Yazılım Mimarisi ve Tasarım Prensipleri
Proje, modülerlik ve kodun yeniden kullanılabilirliği (reusability) ilkelerine sadık kalınarak tasarlanmıştır:

* **Kalıtım (Inheritance):** `Item` ve `Npc1` soyut (abstract) sınıflarından türetilen uzmanlaşmış alt sınıflar (`KeyItem`, `PotionItem`, `EnemyNPC` vb.).
* **Çok Biçimlilik (Polymorphism):** `onUse()` ve `talk()` gibi metotların, nesnenin türüne göre (Örn: İksir vs. Anahtar) farklı davranışlar sergilemesi.
* **Kompozisyon (Composition):** `Room` sınıfının içinde `Item` ve `NPC` koleksiyonlarını barındırması; `GameEngine` sınıfının tüm dünyayı (Map) yönetmesi.
* **Özel Mekanikler:** `Room` sınıfından türetilen `TrapRoom` ile odaya giriş anında tetiklenen özel olay (event) yönetimi.

## 🛠️ Teknik Bileşenler
| Sınıf Grubu | Açıklama |
| :--- | :--- |
| **GameEngine** | Oyunun "beyni". Komut işleme (Command Parsing), harita kurulumu ve dünya durum yönetimi. |
| **Conversation System** | `ConversationNode` yapısı ile NPC'ler için ağaç yapılı (Tree Structure) diyalog sistemi. |
| **Inventory System** | Oyuncunun eşya toplama, kuşanma (`WeaponItem`) ve kullanma mekaniklerini yönetir. |
| **Map System** | Manuel ID atamalı odalar ve kilitli geçit (`LockedExits`) mantığına sahip harita yapısı. |

## 🚀 Öne Çıkan Mühendislik Çözümleri
- **Hata Yönetimi:** Geçersiz komutlar, sayısal olmayan girişler ve kaynak sızıntılarını önleyen `try-catch` ve `Scanner` yönetimi.
- **Esnek Diyalog Yönetimi:** NPC diyalogları, seçeneklere göre farklı dallara ayrılan bir düğüm (node) yapısı ile kurgulanmıştır.
- **Dinamik Etkileşim:** Odalardaki kilitli kapıların, envanterdeki spesifik anahtarlar ile etkileşime girmesini sağlayan `unlockExit` mekanizması.

## 📄 Teknik Rapor ve Analiz
Projenin sınıf tasarımlarını, tasarım gerekçelerini ve test senaryolarını içeren detaylı mühendislik raporuna buradan ulaşabilirsiniz:
👉 **[Proje Teknik Raporu (PDF)](./reports/Abdullah_Batur_Demir_Adventure_Game_Report.pdf)**

## 🎮 Kullanılabilir Komutlar
- `go [yön]` : Kuzey, güney, batı veya doğu yönlerine hareket eder.
- `take [eşya]` : Odadaki eşyayı envantere ekler.
- `use [eşya]` : Eşyayı tüketir veya kuşanır.
- `talk [npc]` : Karakterle diyalog başlatır.
- `inv` : Oyuncu durumunu ve çantasını listeler.
