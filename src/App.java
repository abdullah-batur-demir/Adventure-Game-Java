// Programın başlangıç noktası. GameEngine'nin başlatılması ve ana döngünün çağırılması.
 
public class App {
    public static void main(String[] args) {
        // Oyun motoru nesnesinin oluşturulması
        GameEngine game = new GameEngine();
        
        // Oyunun başlatılması ve dünya haritasının kurulması
        game.initGame();

        // Ana oyun döngüsünün çalıştırılması
        game.startGameLoop();
    }
}