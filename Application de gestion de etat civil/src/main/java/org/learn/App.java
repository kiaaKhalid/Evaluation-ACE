package ma.projet.test;

import org.learn.beans.Femme;
import org.learn.beans.Homme;
import org.learn.beans.Mariage;
import org.learn.Services.FemmeService;
import org.learn.Services.HommeService;
import org.learn.Services.MariageService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App {

    public static void main(String[] args) {

        // Services
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // =========================================================
            // 1. CRÉATION DES DONNÉES DE TEST
            // =========================================================
            System.out.println("Création des données de test...");

            // Hommes (5)
            Homme h1 = new Homme("SAFI", "SAID", "0611", "Rabat", sdf.parse("1970-01-01"));
            Homme h2 = new Homme("ALAMI", "MOHAMED", "0622", "Casa", sdf.parse("1980-02-02"));
            Homme h3 = new Homme("KADIRI", "AHMED", "0633", "Fes", sdf.parse("1985-03-03"));
            Homme h4 = new Homme("BOUZID", "YOUSSEF", "0644", "Tanger", sdf.parse("1990-04-04"));
            Homme h5 = new Homme("RACHIDI", "OMAR", "0655", "Agadir", sdf.parse("1992-05-05"));
            hs.create(h1); hs.create(h2); hs.create(h3); hs.create(h4); hs.create(h5);

            // Femmes (10)
            Femme f1 = new Femme("SALIMA", "RAMI", "0711", "Rabat", sdf.parse("1975-01-01"));
            Femme f2 = new Femme("AMAL", "ALI", "0722", "Rabat", sdf.parse("1980-02-02"));
            Femme f3 = new Femme("WAFA", "ALAOUI", "0733", "Rabat", sdf.parse("1982-03-03"));
            Femme f4 = new Femme("KARIMA", "ALAMI", "0744", "Rabat", sdf.parse("1972-04-04")); // La plus âgée
            Femme f5 = new Femme("HIND", "FILALI", "0755", "Casa", sdf.parse("1985-05-05"));
            Femme f6 = new Femme("SANAA", "ZAIRI", "0766", "Fes", sdf.parse("1988-06-06"));
            // f7 sera mariée 2 fois
            Femme f7 = new Femme("IKRAM", "BENNIS", "0777", "Tanger", sdf.parse("1990-07-07"));
            Femme f8 = new Femme("LINA", "CHRAIBI", "0788", "Agadir", sdf.parse("1993-08-08"));
            Femme f9 = new Femme("RANIA", "MRABET", "0799", "Casa", sdf.parse("1995-09-09"));
            Femme f10 = new Femme("SARA", "LAHLOU", "0700", "Rabat", sdf.parse("1996-10-10"));

            // Enregistrement des femmes
            fs.create(f1); fs.create(f2); fs.create(f3); fs.create(f4); fs.create(f5);
            fs.create(f6); fs.create(f7); fs.create(f8); fs.create(f9); fs.create(f10);

            // Mariages (pour tester la polygamie et les divorces)

            // H1 (SAFI SAID) : 4 mariages en cours, 1 échoué (total 5)
            // Mariages en cours (pour tester Criteria API >= 4)
            ms.create(new Mariage(sdf.parse("1990-09-03"), 4, h1, f1));
            ms.create(new Mariage(sdf.parse("1995-09-03"), 2, h1, f2));
            ms.create(new Mariage(sdf.parse("2000-11-04"), 3, h1, f3));
            ms.create(new Mariage(sdf.parse("2010-01-01"), 1, h1, f10)); // 4ème mariage en cours

            // Mariage échoué (divorce)
            Mariage mDivorceH1 = new Mariage(sdf.parse("1989-09-03"), 0, h1, f4);
            mDivorceH1.setDateFin(sdf.parse("1990-09-03")); // Divorce
            ms.create(mDivorceH1);

            // H2 (ALAMI MOHAMED) : 1 mariage en cours
            ms.create(new Mariage(sdf.parse("2005-05-05"), 1, h2, f5));

            // F7 (IKRAM BENNIS) : Mariée 2 fois (test HQL)
            // 1er mariage (échoué)
            Mariage mF7_1 = new Mariage(sdf.parse("2010-01-01"), 0, h3, f7);
            mF7_1.setDateFin(sdf.parse("2012-01-01"));
            ms.create(mF7_1);
            // 2ème mariage (en cours)
            ms.create(new Mariage(sdf.parse("2014-01-01"), 1, h4, f7));

            System.out.println("Données de test créées.");

            // =========================================================
            // 2. EXÉCUTION DES TESTS
            // =========================================================

            System.out.println("\n#### Test 1 : Liste des femmes ####");
            List<Femme> allFemmes = fs.findAll();
            for(Femme f : allFemmes) {
                System.out.println(f.getId() + ". " + f.getNom() + " " + f.getPrenom());
            }

            System.out.println("\n#### Test 2 : Femme la plus âgée ####");
            Femme plusAgee = fs.findFemmePlusAgee();
            System.out.println("La femme la plus âgée est : " + plusAgee.getNom() + " " + plusAgee.getPrenom() +
                    " (Née le " + sdf.format(plusAgee.getDateNaissance()) + ")");

            System.out.println("\n#### Test 3 : Épouses de H1 (SAFI SAID) actives en 1990 ####");
            Date d1_1990 = sdf.parse("1990-01-01");
            Date d2_1990 = sdf.parse("1990-12-31");
            // Doit afficher f1 (mariée en 09/1990) et f4 (divorcée en 09/1990)
            List<Femme> epousesH1 = hs.findEpousesEntreDates(h1, d1_1990, d2_1990);
            for(Femme f : epousesH1) {
                System.out.println(" - " + f.getNom() + " " + f.getPrenom());
            }

            System.out.println("\n#### Test 4 : Nbr d'enfants de F1 (SALIMA RAMI) (Mariages débutés 1990-1995) ####");
            Date d1_1990_1995 = sdf.parse("1990-01-01");
            Date d2_1990_1995 = sdf.parse("1995-12-31");
            // f1 a eu 2 mariages (fictifs), mais un seul a commencé dans cette plage (celui de 1990 avec 4 enfants)
            long nbrEnfants = fs.countEnfants(f1, d1_1990_1995, d2_1990_1995);
            System.out.println("Nombre d'enfants (Native Query) : " + nbrEnfants);

            System.out.println("\n#### Test 5 : Femmes mariées 2 fois ou plus (HQL Nommée) ####");
            // Doit afficher f7 (IKRAM BENNIS)
            List<Femme> femmesMultiples = fs.findFemmesMarieesDeuxFois();
            for(Femme f : femmesMultiples) {
                System.out.println(" - " + f.getNom() + " " + f.getPrenom());
            }

            System.out.println("\n#### Test 6 : Hommes mariés à 4+ femmes (Actifs en 2024) (Criteria API) ####");
            Date d1_2024 = sdf.parse("2024-01-01");
            Date d2_2024 = sdf.parse("2024-12-31");
            // Doit trouver h1 (SAFI SAID) qui a 4 mariages en cours
            long nbrHommes = fs.countHommesMarriesQuatreFemmes(d1_2024, d2_2024);
            System.out.println("Nombre d'hommes (Criteria API) : " + nbrHommes);

            System.out.println("\n#### Test 7 : Affichage détaillé des mariages de H1 (SAFI SAID) ####");
            // Teste la méthode d'affichage de HommeService
            hs.afficherMariages(h1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}