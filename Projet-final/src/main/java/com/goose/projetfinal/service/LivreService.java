import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.stereotype.Service;

@Service
public class LivreService {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    public LivreService(LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
    }

    // Écrivez ici les méthodes du tableau
    public List<Livre> recupererTous() {
        return livreRepository.findAll();
    }

    public Optional<Livre> recupererParId(Long id) {
        return livreRepository.findById(id);
    }

    public Optional<List<Livre>> recupererParAuteurId(Long id) {
        return livreRepository.findByAuteurId(id);
    }

    public Livre creer(Livre livre) {
        return livreRepository.save(livre);
    }

    public Livre mettreAJour(Long id, Livre livre) {
        return livreRepository.findById(id)
                .map(livreExistant -> {
                    livreExistant.setTitre(livre.getTitre());
                    livreExistant.setIsbn(livre.getIsbn());
                    livreExistant.setPrix(livre.getPrix());
                    livreExistant.setAnneePublication(livre.getAnneePublication());
                    livreExistant.setStock(livre.getStock());
                    livreExistant.setAuteurId(livre.getAuteurId());
                    return livreRepository.save(livreExistant);
                })
                .orElseThrow(() -> new EntityNotFoundException("Livre non trouvé"));
    }

    public boolean supprimer(Long id) {
        if (livreRepository.existsById(id)) {
            livreRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

}
