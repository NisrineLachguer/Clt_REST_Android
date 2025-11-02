package ma.projet.restclient.repository;

import ma.projet.restclient.api.CompteService;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.entities.CompteList;
import ma.projet.restclient.config.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteRepository {
    private CompteService compteService;
    private String format;

    // Constructeur pour initialiser le service avec le type de convertisseur
    public CompteRepository(String converterType) {
        compteService = RetrofitClient.getClient(converterType).create(CompteService.class);
        this.format = converterType;
    }

    // Méthode pour récupérer tous les comptes
    public void getAllCompte(Callback<List<Compte>> callback) {
        if ("JSON".equals(format)) {
            Call<List<Compte>> call = compteService.getAllCompteJson();
            call.enqueue(callback);
        } else {
            Call<CompteList> call = compteService.getAllCompteXml();
            call.enqueue(new Callback<CompteList>() {
                @Override
                public void onResponse(Call<CompteList> call, Response<CompteList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Convertir CompteList en List<Compte>
                        List<Compte> comptes = response.body().getComptes();
                        callback.onResponse(null, Response.success(comptes));
                    }
                }

                @Override
                public void onFailure(Call<CompteList> call, Throwable t) {
                    callback.onFailure(null, t);
                }
            });
        }
    }

    // Méthode pour récupérer un compte par son ID
    public void getCompteById(Long id, Callback<Compte> callback) {
        Call<Compte> call = compteService.getCompteById(id);
        call.enqueue(callback);
    }

    // Méthode pour ajouter un compte - TOUJOURS EN JSON
    public void addCompte(Compte compte, Callback<Compte> callback) {
        // Utiliser JSON pour l'ajout, peu importe le format de lecture
        CompteService jsonService = RetrofitClient.getClient("JSON").create(CompteService.class);
        Call<Compte> call = jsonService.addCompte(compte);
        call.enqueue(callback);
    }

    // Méthode pour mettre à jour un compte - TOUJOURS EN JSON
    public void updateCompte(Long id, Compte compte, Callback<Compte> callback) {
        // Utiliser JSON pour la modification, peu importe le format de lecture
        CompteService jsonService = RetrofitClient.getClient("JSON").create(CompteService.class);
        Call<Compte> call = jsonService.updateCompte(id, compte);
        call.enqueue(callback);
    }

    // Méthode pour supprimer un compte
    public void deleteCompte(Long id, Callback<Void> callback) {
        Call<Void> call = compteService.deleteCompte(id);
        call.enqueue(callback);
    }
}