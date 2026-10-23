import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientBibliotheque {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static int nombreEtapes = 0;
    private static int nombreConformes = 0;

    private static HttpResponse<String> get(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> post(String chemin, String corpsJson) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpsJson))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> patch(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> delete(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + chemin))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void afficher(String description, String methode, String chemin, HttpResponse<String> reponse, int codeAttendu) {
        nombreEtapes++;
        boolean conforme = reponse.statusCode() == codeAttendu;
        if (conforme) {
            nombreConformes++;
        }
        System.out.println("=== Étape " + nombreEtapes + " : " + description + " ===");
        System.out.println(methode + " " + chemin + " → " + reponse.statusCode() + " (attendu : " + codeAttendu + ") " + (conforme ? "CONFORME" : "NON CONFORME"));
        System.out.println("Réponse : " + reponse.body());
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        // Étape 1 : Lister les 3 premiers livres disponibles
        afficher(
                "Lister les 3 premiers livres disponibles",
                "GET",
                "/livres/disponibles?size=3",
                get("/livres/disponibles?size=3"),
                200
        );

        // Étape 2 : Lire un livre inexistant
        afficher(
                "Lire un livre inexistant",
                "GET",
                "/livres/99",
                get("/livres/99"),
                404
        );

        // Étape 3 : Créer l'auteur Jules Verne
        String auteurJson = """
                {
                  "nom": "Verne",
                  "prenom": "Jules",
                  "nationalite": "Française"
                }
                """;
        afficher(
                "Créer l'auteur Jules Verne",
                "POST",
                "/auteurs",
                post("/auteurs", auteurJson),
                201
        );

        // Étape 4 : Emprunter le livre 2
        String empruntJson = """
                {
                  "livreId": 2,
                  "emprunteur": "Alice Durand",
                  "emailEmprunteur": "alice.durand@email.com"
                }
                """;
        afficher(
                "Emprunter le livre 2",
                "POST",
                "/emprunts",
                post("/emprunts", empruntJson),
                201
        );

        // Étape 5 : Emprunter le livre 8 (stock à 0)
        String empruntLivre8Json = """
                {
                  "livreId": 8,
                  "emprunteur": "Alice Durand",
                  "emailEmprunteur": "alice.durand@email.com"
                }
                """;
        afficher(
                "Emprunter le livre 8 (stock à 0)",
                "POST",
                "/emprunts",
                post("/emprunts", empruntLivre8Json),
                409
        );

        // Étape 6 : Rendre l'emprunt 2
        afficher(
                "Rendre l'emprunt 2",
                "PATCH",
                "/emprunts/2/retour",
                patch("/emprunts/2/retour"),
                200
        );

        // Étape 7 : Rendre l'emprunt 2 une deuxième fois
        afficher(
                "Rendre l'emprunt 2 une deuxième fois",
                "PATCH",
                "/emprunts/2/retour",
                patch("/emprunts/2/retour"),
                409
        );

        // Étape 8 : Supprimer l'auteur 1 (il a des livres)
        afficher(
                "Supprimer l'auteur 1 (il a des livres)",
                "DELETE",
                "/auteurs/1",
                delete("/auteurs/1"),
                409
        );

        // Étape 9 : Créer un livre invalide (titre vide, prix négatif)
        String livreInvalideJson = """
                {
                  "titre": "",
                  "isbn": "978-2-0000-0000-0",
                  "prix": -5,
                  "stock": 1,
                  "auteurId": 1
                }
                """;
        afficher(
                "Créer un livre invalide (titre vide, prix négatif)",
                "POST",
                "/livres",
                post("/livres", livreInvalideJson),
                400
        );

        // Étape 10 : Supprimer l'auteur 5 (aucun livre)
        afficher(
                "Supprimer l'auteur 5 (aucun livre)",
                "DELETE",
                "/auteurs/5",
                delete("/auteurs/5"),
                204
        );

        System.out.println("Bilan : " + nombreConformes + "/" + nombreEtapes + " étapes conformes");
    }
}
