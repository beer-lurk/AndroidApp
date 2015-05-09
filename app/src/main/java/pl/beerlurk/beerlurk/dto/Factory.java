package pl.beerlurk.beerlurk.dto;

import java.util.Arrays;
import java.util.List;

public final class Factory {

    public static BeerLocationsWrapper create() {
        List<BeerLocation> locations = Arrays.asList(
                new BeerLocation("Warszawa, Miedziana 1B, Dla Smakosza", null),
                new BeerLocation("Warszawa, Al. KEN 50 lok.U6, Piwonia. Piwa z duszą", null),
                new BeerLocation("Warszawa, Aspekt 79, Leclerc", null),
                new BeerLocation("Warszawa, Gustawa Morcinka 5 lok 6 (Bemowo), Piwa Regionalne", null),
                new BeerLocation("Warszawa, Wilanów, Al. Rzeczpospolitej 17/U2 Ergo bibamus. Piwo i inne przyjemności.", null),
                new BeerLocation("Warszawa, Al. Jana Pawła II 61 (róg Anielewicza), PIWOmaniak", null),
                new BeerLocation("Piwa z Azji", null),
                new BeerLocation("Warszawa, Żeromskiego 17C (Bielany), Stan Umysłu - sklep i galeria z dobrym piwem", null),
                new BeerLocation("Warszawa, Kabacki Dukt 4 lok. U3, Czarodziejka Gorzałka", null),
                new BeerLocation("Warszawa, Grupy AK Północ 4 lok usł 3 (wejście od Bartyckiej), ALKOHOLE 24H", null),
                new BeerLocation("Warszawa, Żelazna 59A, Top Market \"Perełka\"", null),
                new BeerLocation("Warszawa, Wyspowa 8 (Targówek), Ósemka", null),
                new BeerLocation("Warszawa, Batalionu „Włochy” 20b lok. 9a (Włochy), Świat Alkoholi", null),
                new BeerLocation("Warszawa, Woronicza 31 lok. U12, Viski", null),
                new BeerLocation("Warszawa, Skoroszewska 5C lok. 5U, Chmiel i Żyto", null),
                new BeerLocation("Warszawa, Olbrachta 18, Pod Strzechą", null),
                new BeerLocation("Warszawa, Al. Jana Pawła II 41a lok.3, Salon Alkoholi 24", null),
                new BeerLocation("Warszawa, Cynamonowa 19, Wino i Spółka", null),
                new BeerLocation("Warszawa, Rydygiera 13, Das Bier", null),
                new BeerLocation("Warszawa, Jutrzenki 156, Leclerc", null),
                new BeerLocation("Warszawa, Wyspowa 2/U2 (Targówek), Łowcy Procentów", null),
                new BeerLocation("Warszawa, Jagiellońska 3, Odido", null),
                new BeerLocation("Siedlce, Łukowska 42, sklep z alkoholem Beata", null),
                new BeerLocation("Warszawa, Potocka 14 (pawilony), Rybka lubi pływać 24", null)
        );
        return new BeerLocationsWrapper(locations);
    }
}
