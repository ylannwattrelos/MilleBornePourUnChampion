class Players{
    /*
    String voiture est la voiture utilisé par le joueur;
    int position_Plateau représente la poisition actuel du joueur sur le plateau (1 = 1KM);
    Cards[] jeu représente la main d un joueur (les cartes qu il a actuellement dans sa main);
    String pseudo représente le pseudo du joueur choisi au début du jeu;
    index_vide représente l index du tableau jeu où le joueur n a pas de carte (positionnement de la derniere carte joué);
    Malus malus représente les malus que le joueur subis ou non;
    */
    String pseudo;
    int numero;
    String voiture;
    int position_Plateau=0;
    Cards[] jeu = new Cards[7];
    int index_vide=6;
    Malus malus;
}