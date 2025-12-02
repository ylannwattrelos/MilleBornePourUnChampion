import extensions.*;  
class MilleBorne extends Program {

    final String LOGO = "ressources/message.txt";
    // les voitures en String car en Char ils n existent pas
    final String[] VOITURES_DISPO = new String[]{"🚗","🚙","🚕","🚐","🚓"};
    // Delay d affichage de chaque carac
    final int DELAY = 25;
    // noms des cartes , leur associations en valeur et leur associations en nombre;
    final NameCards[] CARTES_BORNE = new NameCards[]{ NameCards.BORNES_50,  NameCards.BORNES_100 , NameCards.BORNES_150 , NameCards.BORNES_200};
    final NameCards[] MALUS = new NameCards[] { NameCards.CREVAISON , NameCards.FEU_ROUGE , NameCards.LIMIT_50  , NameCards.LIMIT_100 , NameCards.ACCIDENT};
    final NameCards[] BONUS = new NameCards[]{ NameCards.ROUES  , NameCards.FEU_VERT , NameCards.NO_LIMIT , NameCards.GARAGE};
    // nb de cartes
    final int nombre_CARTES_BORNEs = 35;
    final int nombre_cartes_malus = 100;
    final String RESET = "\033[0m";
    final String ROUGE = "\033[31m";
    final String VERT = "\033[32m";
    final String JAUNE = "\033[33m";
    final String BLEU = "\033[34m";
    final String MAGENTA = "\033[35m";
    final String CYAN = "\033[36m";

    void algorithm() {
        clearScreen();
        afficherFichier(LOGO);
        print("Appuyer sur \"entrée\" pour commencer à jouer");
		readString();
        clearScreen();
        welcome();
        start();
    }

    //Affichage du contenu d'un fichier texte d'après son chemin
	void afficherFichier(String chemin){
		File unTexte = newFile(chemin);

		//Stockage dans une variable de la ligne suivante dans le fichier
		while(ready(unTexte)){
			//affichage du contenu de la ligne suivante
			println(readLine(unTexte));
		}
	}
    
    void start(){
        Plateau plateau = initJeu();
        int joueur_actuel = 0;
        while (!partieFinie(plateau)) {

            clearScreen();
            print(toString(plateau,joueur_actuel));
            println("Tour de "+plateau.liste_joueurs[joueur_actuel].pseudo + ", vous êtes le joueur "+(joueur_actuel + 1 ) +"\n");
            println("Les malus du joueur sont : "+toString(plateau.liste_joueurs[joueur_actuel].malus)+"\n");
            tourJoueur(plateau.liste_joueurs[joueur_actuel] , plateau);
            //delay(3000);
            joueur_actuel = (joueur_actuel+1) % length(plateau.liste_joueurs);
            if (piocheVide(plateau)) {
                initPioche(plateau);
            }
        }
        println("GG le joueur"+ getWinner(plateau) + " a gagné");

    }
    boolean piocheVide(Plateau plat){
        return plat.nb_cartes_pioche == 0;
    }
    String getWinner(Plateau plat){
        int i = 0;
        while (i < length(plat.liste_joueurs) && plat.liste_joueurs[i].position_Plateau >= 1000) {
            i ++;
        }
        return plat.liste_joueurs[i].pseudo;
    }
    void tourJoueur(Players joueur , Plateau plat){
        joueur.jeu[joueur.index_vide] =  piocher(plat);
        println(toString(joueur.jeu));
        int choix = saisir("numéro de la carte a joué: ",1,7) -1;
        println("la carte joué est "+joueur.jeu[choix].nom);
        if(jouerCarte(joueur.jeu[choix] , joueur , plat)){
            delayPrint("La carte est joué avec success\n",DELAY+10);
            delay(1000);
        }else{
            delayPrint("la carte a été defaussé\n",DELAY+10);
        }
        joueur.index_vide = choix;
        joueur.jeu[choix] = null;
    }

    Plateau initJeu(){
        int nbJoueurs = saisir("Entrez un nombre de joueurs : ", 2, 4);
        Plateau plateau = newPlateau(nbJoueurs);
        initPioche(plateau);
        initQuestions(plateau);
        initJoueurs(plateau);
        distribuerCartes(plateau);
        return plateau;
    }

    //initialisations 

    void initPioche(Plateau plateau){
        // fonction pour initialiser une pioche pour le plateau p , aleatoire
        plateau.nb_cartes_pioche = 0;
        
        int idx = 0;
        CSVFile file = loadCSV("./ressources/stats.csv");
        for (int i = 1; i < rowCount(file); i++) {
            plateau.nb_cartes_pioche += stringToInt(getCell(file,i,1));
        }
        
        plateau.pioche = new Cards[plateau.nb_cartes_pioche];

        for (int i = 0; i < length(CARTES_BORNE); i++) {
            for (int u = 0; u < getStatPerName(CARTES_BORNE[i]); u++) {
                plateau.pioche[idx] = newCards(CARTES_BORNE[i], valeurCarte(CARTES_BORNE[i]), true , valeurDifficulte(CARTES_BORNE[i]));
                idx ++;
            }
        }
        for (int i = 0; i < length(MALUS); i++) {
            for (int u = 0; u < getStatPerName(MALUS[i]); u++) {
                plateau.pioche[idx] = newCards(MALUS[i], valeurCarte(MALUS[i]), false , valeurDifficulte(MALUS[i]));
                idx ++;
            }
        }
        for (int i = 0; i < length(BONUS); i++) {
            for (int u = 0; u < getStatPerName(BONUS[i]); u++) {
                plateau.pioche[idx] = newCards(BONUS[i], valeurCarte(BONUS[i]), false , valeurDifficulte(BONUS[i]));
                idx ++;
            }
        }
        melanger(plateau.pioche);
    }

    void melanger(Cards[] paquet){
        // fonction qui permet de mélanger un paquet de carte donné
        for (int i = 0; i < length(paquet); i++) {
            int index_choix = (int) (random() * length(paquet));
            Cards carte_choisi = paquet[index_choix];
            paquet[index_choix] = paquet[i];
            paquet[i] = carte_choisi;
        }
    }
    void melanger(Question[] paquet){
        // fonction qui permet de mélanger un paquet de question donné
        for (int i = 0; i < length(paquet); i++) {
            int index_choix = (int) (random() * length(paquet));
            Question carte_choisi = paquet[index_choix];
            paquet[index_choix] = paquet[i];
            paquet[i] = carte_choisi;
        }
    }
    int valeurCarte(NameCards nom){
        for (int i = 0; i < length(CARTES_BORNE); i++) {
            if(nom == CARTES_BORNE[i]){
                return (i+1) * 50;
            }
        }
        return 0;
    }
    int valeurDifficulte(NameCards nom){
        for (int i = 0; i < length(CARTES_BORNE); i++) {
            if(nom == CARTES_BORNE[i]){
                return i+1;
            }
        }
        return 0;
    }
    boolean estCarteBorne(NameCards nom){
        return valeurCarte(nom) > 0;
    }
    boolean isBorne(Cards carte){
        return carte.borne_carte;
    }
    void initJoueurs(Plateau plat){
        // fonction qui demande les infos de chaque joueurs
        for (int i = 0; i < length(plat.liste_joueurs); i++) {
            clearScreen();
            print("Bonjour joueur "+ (i+1) + " ! Veuillez entrer votre pseudo : ");
            String pseudo = saisiePseudo(plat);
            println("1  2  3  4  5");
            println(toString(VOITURES_DISPO));
            int nb_Voiture = saisir("Veuillez choisir un vehicule : ",1,length(VOITURES_DISPO));
            plat.liste_joueurs[i] = newPlayers(i, pseudo , VOITURES_DISPO[nb_Voiture-1]);
        }
    }

    void distribuerCartes(Plateau plat){
        for (int nbCartesDonne = 0; nbCartesDonne < 6; nbCartesDonne++) {
            for (int joueur_actuel = 0; joueur_actuel < length(plat.liste_joueurs); joueur_actuel++) {
                plat.liste_joueurs[joueur_actuel].jeu[nbCartesDonne] = piocher(plat);
            }
        }
    }

    Cards piocher(Plateau p){
        p.nb_cartes_pioche -= 1;
        return p.pioche[p.nb_cartes_pioche];
    }

    void testGetVoiture(){
        assertEquals("🚗 🚙 🚕 🚐 🚓 " , toString(VOITURES_DISPO));
    }

    Plateau newPlateau(int  nbJoueurs){
        // fonction pour gen le plateau
            Plateau plat = new Plateau();
            plat.liste_joueurs = new Players[nbJoueurs];
            return plat;
    }

    Cards newCards(NameCards name , int value, boolean borne_carte, int difficulte){
        Cards carte = new Cards();
        carte.nom = name;
        carte.valeurDeDéplacement = value;
        carte.borne_carte = borne_carte;
        carte.difficulte = difficulte;
        return carte;
    }

    Players newPlayers(int numero , String pseudo , String voiture ){
        /* Fonction de construction du joueur */
        Players joueur = new Players();
        joueur.numero = numero;
        joueur.pseudo = pseudo;
        joueur.voiture = voiture;
        joueur.malus = new Malus();
        return joueur;
    }
    String saisiePseudo(Plateau p){
        print("Saissisez un pseudo :");
        String saisie = readString();
        while (length(saisie) <= 0 || length(saisie) >= 10 || exist(saisie,p)){
            println("Votre pseudo ne doit pas être déjà utilisé et doit faire entre 1 et 10 caractères.");
            print("Saissisez un pseudo :");
            saisie = readString();
        }
        return saisie;
    }

    boolean exist(String input , Plateau plat){
        int i = 0;
        while (i < length(plat.liste_joueurs)) {
            if (plat.liste_joueurs[i] != null) {
                if (equals(plat.liste_joueurs[i].pseudo,input)) {
                    return true;
                }
            }else{
                return false;
            }
            i++;
        }
        return (i != length(plat.liste_joueurs));
    }

    int saisir(String message ,int min , int max){
        /*
        fonction de controle de saisie;
        message (String): Message à demandé à l'utilisateur;
        min (int): Nombre minimum que l'utilisateur doit entré (inclus);
        max (int): Nombre maximum que l'utilisateur peut entré (inclus);
        return (int) : nombre saisie par l'utilisateur;
        */
            print(message);
            String saisie = readString();
            while (length(saisie) <= 0 || !onlyNumbers(saisie) || stringToInt(saisie) < min || stringToInt(saisie) > max){
                print("Nombre entré invalide (doit être entre " + min + " et " + max+")");
                saisie = readString();
            }
            return stringToInt(saisie);
    }

    boolean onlyNumbers(String msg){
        int i = 0;
        while ( i < length(msg) && charAt(msg,i) >= '0' && charAt(msg,i) <= '9') {
            i++;
        }
        return i == length(msg);
    }

    void testGenerateRoute(){
        Players p = newPlayers(1,"J1","🚗");
        p.position_Plateau = 1000;
        assertEquals("   __________________________________________________ \n  |                                                  |\n  |                                                🚗|\n  |__________________________________________________|",generateRoute(p));
        Players p3 = newPlayers(1,"J3","🚐");
        p3.position_Plateau = 485;
        assertEquals("   __________________________________________________ \n  |                                                  |\n  |                      🚐                          |\n  |__________________________________________________|",generateRoute(p3));
    }

    String generateRoute(Players joueur) {
    /*
     Fonction generateRoute qui genere la route d un joueur ainsi que sa position.
     joueur (Players) : objet Joueur.
     return (String): La route avec la voiture à la bonne position;
    */
        int position = joueur.position_Plateau / 20;
        String msg = "   __________________________________________________ \n  |                                                  |\n";
    
        // Espaces avant la voiture
        if(position == 0){
            msg += joueur.voiture +"|                                                  | \n  |__________________________________________________|";
            return msg;
        }else if(position == 1){
            msg += "  " + joueur.voiture +"                                                 | \n  |__________________________________________________|";
            return msg;
        }else{
            msg += "  |";
            for (int i = 0; i < position - 2; i++) {
                msg += " ";
            }
        
            // Ajouter la voiture
            msg += joueur.voiture;
        
            // Espaces après la voiture (ajusté pour que la voiture ne dépasse pas)
            int espace = 50 - position;
            for (int i = 0; i < espace; i++) { // Ajustement de l espace après la voiture;
                msg += " ";
            }
        
            msg += "|\n  |__________________________________________________|";
        }
        return msg;
    }
   
    Question newQuestion(String question , String reponse , int niveau , String sujet){
        Question quest = new Question();
        quest.question = question;
        quest.reponse = reponse;
        quest.niveau = niveau;
        quest.sujet = sujet;
        return quest;
    }

    void initQuestions(Plateau P){
        //saveCSV( new String[][]{{"OUOU"},{"AA"}} , "./ressources/caca.csv");
        CSVFile[] file = new CSVFile[]{loadCSV("./ressources/questionv1.csv",';'),loadCSV("./ressources/questionv2.csv",';'),loadCSV("./ressources/questionv3.csv",';'),loadCSV("./ressources/questionv4.csv",';')};
        Question[][] tabquestion = new Question[length(file)][];
        for (int idx = 0; idx < length(file); idx++) {
            tabquestion[idx] = new Question[rowCount(file[idx])-1];
            for (int i = 0; i < rowCount(file[idx])-1 ; i++) {
                tabquestion[idx][i] = newQuestion(getCell(file[idx],i+1,0) , getCell(file[idx],i+1,1) , 1 , getCell(file[idx],i+1,2));
            }
        }
        P.questions = tabquestion;
    }



    // Les fonctions toString des differents nouveaux type

    String toString(Cards[] paquet){
        String[][] msg = new String[7][8]; // 7 cartes qui prennent 7 lignes;
        for (int i = 0; i < length(paquet); i++) {
            if(paquet[i] != null){
                msg[i] = stringToArray(toString(paquet[i],i+1));
            }
        }
        return toString(msg);
    }


    void testStringToArray(){
        String[][] tab1 = new String[][]{{"1erl","2emel"},{"2emecarte","2emecarte"}};
        String[][] tab2 = new String[][]{stringToArray("1erl\n2emel"),stringToArray("2emecarte\n2emecarte")};
        assertArrayEquals(tab1[1],tab2[1]);
        tab1 = new String[][]{{"abc","pp"},{"def","gh"}};
        tab2 = new String[][]{stringToArray("abc\npp"),stringToArray("def\ngh")};
        assertArrayEquals(tab1[1],tab2[1]);
    }
    String toString(String[][] tab_double) {
        String msg = "";
        for (int u = 0; u < length(tab_double[0]); u++) {
            for (int i = 0; i < length(tab_double); i++) { 
                msg += tab_double[i][u] + " "; 
            }
            msg += "\n";
        }
        return msg;
    }

    String[] stringToArray(String msg){
        //fonction qui transforme un de string en un tab de String. Chaque entrée est une ligne de la chaine
        int nb_retour = 0;
        String[] tab = new String[length(msg)];
        for (int i = 0; i < length(msg); i++) {
            if(charAt(msg,i) == '\n'){
                nb_retour += 1;
            }else{
                if (tab[nb_retour] == null) {
                    tab[nb_retour] = charAt(msg,i) +"";
                }else{
                    tab[nb_retour] = tab[nb_retour] + charAt(msg,i);
                }
                
            }
        }
        return slice(tab);
    }

    Question[] slice(Question[] tableau){
            /* Fonction qui renvoi un tableau sans les valeur null en fin de tab;
            (String[]) tableau: tableau à decouper;
            (int) start: debut du decoupage;
            (int) stop: fin du decoupage (exclu);
            */
        int nb = 0;
        for (int i = 0; i < length(tableau); i++) {
            if (tableau[i] == null){
                nb ++;
            }
        }
        Question[] tab = new Question[length(tableau)-nb];
        for (int i = 0; i < length(tableau)-nb; i++) {
            tab[i] = tableau[i];
        }
        return tab;
    }
    void testSlice(){
        Question[] tableau = {new Question(), new Question(), new Question(), null, null};
        Question[] result = slice(tableau);
        assertEquals(3,length(result));
    }
    String[] slice(String[] tableau){
        /* Fonction qui renvoi un tableau sans les valeur null en fin de tab;
            (String[]) tableau: tableau à decouper;
            (int) start: debut du decoupage;
            (int) stop: fin du decoupage (exclu);
        */
        int nb = 0;
        for (int i = 0; i < length(tableau); i++) {
            if (tableau[i] == null){
                nb ++;
            }
        }
        String[] tab = new String[length(tableau)-nb];
        for (int i = 0; i < length(tableau)-nb; i++) {
            tab[i] = tableau[i];
        }
        return tab;
    }
    void testSliceString(){
        String[] tableau = {"abc","def", "toi" ,"jprefretwa", null, null,null,null};
        String[] result = slice(tableau);
        assertEquals(4,length(result));
    }




    String toString(Cards carte, int numero){
        String msg = "+-------------------+\n";
        if(isBorne(carte)){
            msg = msg + "|      Borne        |\n";
        }else if(estUneCarteBonus(carte)){
            msg = msg + "|      Bonus        |\n";
        }else {
           msg = msg + "|      Malus        |\n";
        }
        msg = msg + "|    +---------+    |\n|    |   n°"+numero+"   |    |\n|    +---------+    |\n|       Carte       |\n|";
        int space = 19 - length(carte.nom +"");
        for (int i = 0; i <= space; i++) {
            if (space/2 == i) {
                msg += carte.nom;
            }else {
                msg += " ";
            }
        }
        return msg + "|\n+-------------------+";
    }


    String toString(String[] voitures){
        /* function qui retourne la liste des voiture en string*/
        String msg = "";
        for (int i = 0; i < length(voitures); i++) {
            msg = msg + voitures[i] + " ";
        }
        return msg;
    }

    String toString(Plateau plat , int joueur_actuel){
        String msg = "";
        for (int i = 0; i < length(plat.liste_joueurs); i++) {
            if(joueur_actuel == i){
                msg += VERT;
            }
            msg +=  generateRoute(plat.liste_joueurs[i]) + RESET + "\n";
        }
        return msg;
    }

    String toString(Question[] quest){
        String msg = "";
        for (int i = 0; i < length(quest); i++) {
            msg += quest[i].question + "\n";
        }
        return msg;
    }
    String toString(NameCards carte){
        return "" + carte;
    }
    String toString(Malus malus){
        String msg = "";
        boolean feu=true;
        boolean crever=false;
        boolean accident=false;
        int limit=200;
        if (malus.feu) {
            msg += "Feu Rouge, ";
        }
        if (malus.crever) {
            msg += "Roue Crevée, ";
        }
        if (malus.accident) {
            msg += "Accident, ";
        }
        msg += "Limité à "+ malus.limit + "KM";
        return msg ;
    }


    boolean estBloquer(Players joueur){
        return joueur.malus.feu || joueur.malus.crever || joueur.malus.accident;
    }


    boolean jouerCarte(Cards cartejoué , Players joueur , Plateau plat){

        if(estCarteBorne(cartejoué.nom)){
            if(!estBloquer(joueur) && !estLimite(joueur,cartejoué)){
                Question question = genQuestion(plat , cartejoué.difficulte);
                if(reponseBonne(cartejoué , plat , question )){
                        avancerDe(joueur , valeurCarte(cartejoué.nom));
                }else{
                    delayPrint("La réponse donnée est fausse. ",DELAY+15);
                    delayPrint("La bonne réponse étais \""+ getReponse(question)+ "\"\n",DELAY+15);
                    println("Appuyez sur \"Entrée\" pour continuer");
                    readString();
                    return false;
                }
            }else{
                return false;
            }
        }else{
            jouerCarteSpecial(cartejoué, joueur , plat);
        }
        return true;
    }
    void avancerDe(Players joueur , int value){
        //fonction pour faire avancer le joueur de value KM
        joueur.position_Plateau += value;
    }

    Question genQuestion(Plateau plat , int niveau){
        return plat.questions[niveau-1][(int)(random() * (length(plat.questions[niveau-1])/4))];
    }

    boolean reponseBonne(Cards carte , Plateau plat , Question question){
       
        println(question.question); // print la question choisie;
        
        Question[] tab_q = getQuestionsPerSubject(plat, getSujet(question),question.niveau);
        goodRepToEnd(tab_q,getReponse(question)); // permet de mettre la bonne réponse a la fin du tableau a fin de ne pas l afficher dans les réponses disponible plus tard;
        Question[] tab_des_reponses = genRandomTab(tab_q,4); // generer une liste de 4 réponses possible;
        tab_des_reponses[3] = question; // mettre la bonne à la derniere position;
        melanger(tab_des_reponses); // mélanger le tableau des questions;
        println(genReponsePossible(tab_des_reponses)); // afficher les réponses disponible;
        int input = saisir("\n>",1,4);
        return equals(getReponse(question),getReponse(tab_des_reponses[input - 1]));
    }
    
    Question[] genRandomTab(Question[] tab_initiale, int nb_questions){
        // gen des question random
        Question[] tab_reponse = new Question[nb_questions];
        for (int i = 1; i < nb_questions; i++) {
            int position = (int)(random() * (length(tab_initiale)- i-1));
            tab_reponse[i-1] = tab_initiale[position];
            tab_initiale[position] = tab_initiale[length(tab_initiale) -1 -i ];
        }
        return tab_reponse;
    }

    String genReponsePossible(Question[] lst){
        String msg = "";
        String[] tab_colors = new String[]{VERT,ROUGE,JAUNE,CYAN};
        for (int i = 0; i < 4; i++) {
            msg += tab_colors[i];
            msg += i+1 +") "+ getReponse(lst[i]) + "\t";
            msg += RESET;
        }
        return msg;
    }

   void goodRepToEnd(Question[] tab , String rep){
        // fonctions pour positionner la bonne réponse à la fin du tableau
        int i = 0;
        while (i < length(tab) && !equals(getReponse(tab[i]),rep)) {
            i ++;
        }
        Question temp = tab[length(tab)-1];
        if (i != length(tab)) {
            tab[length(tab)-1] = tab[i];
            tab[i] = temp;
        }
    }

    Question[] getQuestionsPerSubject(Plateau plat, String theme,int niveau){
        Question[] questions = plat.questions[niveau-1];
        int nb_questions = 0;
        Question[] tab_quest = new Question[length(questions)];
        for (int i = 0; i < length(questions); i++) {
            if (equals(getSujet(questions[i]) , theme) ) {
                tab_quest[nb_questions] = questions[i];
                nb_questions ++;
            }
        }
        return slice(tab_quest);
    }
    String getSujet(Question q){
        return q.sujet;
    }
    String getReponse(Question q){
        return q.reponse;
    }
    boolean estLimite(Players joueur , Cards carte){
        return carte.valeurDeDéplacement > joueur.malus.limit;
    }

    String removeUnChar(String msg){
        String rep = "";
        for (int i = 0; i < length(msg); i++) {
            if ((charAt(msg,i) >= 'a' && charAt(msg,i) <= 'z') || (charAt(msg,i) >= '0' && charAt(msg,i) <= '9')) {
                rep += charAt(msg,i);
            }
        }
        return rep;
    }

    void jouerCarteSpecial(Cards carte , Players joueur , Plateau plat){
        if (estUneCarteBonus(carte)) {
            if (carte.nom == NameCards.ROUES) {
                joueur.malus.crever = false;
            }
            else if (carte.nom == NameCards.FEU_VERT) {
                joueur.malus.feu = false;
            }
            else if (carte.nom == NameCards.NO_LIMIT) {
                joueur.malus.limit = 200;
            }
            else if (carte.nom == NameCards.GARAGE) {
                joueur.malus.accident = false;
            }
        }else{
            int choix = saisir("Quel joueur veut tu ciblé avec ta carte malus ?\n >",1,length(plat.liste_joueurs));
            if (carte.nom == NameCards.CREVAISON) {
                plat.liste_joueurs[choix-1].malus.crever = true;
            }
            else if (carte.nom == NameCards.FEU_ROUGE) {
                plat.liste_joueurs[choix-1].malus.feu = true;
            }
            else if (carte.nom == NameCards.LIMIT_50) {
                plat.liste_joueurs[choix-1].malus.limit = 50;
            }
            else if (carte.nom == NameCards.LIMIT_100) {
                plat.liste_joueurs[choix-1].malus.limit = 100;
            }
            else if (carte.nom == NameCards.ACCIDENT) {
                plat.liste_joueurs[choix-1].malus.accident = true;
            }else{
                println("La carte "+carte.nom+" n'a pas été trouvé");
            }
        }
    }

    boolean estUneCarteBonus(Cards carte){
        int i = 0;
        while (i < length(BONUS) && BONUS[i] != carte.nom) {
            i ++;
        }
        return i != length(BONUS);
    }

    boolean partieFinie(Plateau p){
        int i = 0;
        while (i < length(p.liste_joueurs) && p.liste_joueurs[i].position_Plateau <= 1000) {
            i++;
        }
        return i != length(p.liste_joueurs);
    }
    void welcome() {
        delayPrint(VERT+"Bienvenue dans le jeu du Mille Bornes !\n"+RESET,DELAY);
        delayPrint(BLEU+"C'est un jeu de cartes où le but est de parcourir 1000 bornes en premier 🚗.\n"+RESET,DELAY);
        delayPrint(JAUNE+"Vous pouvez utiliser des malus pour mettre des bâtons dans les roues de vos adversaires 🚧, comme des crevaisons ou des accidents.\n"+RESET,DELAY);
        delayPrint(VERT+"Les bonus vous permettent de contrer les malus que vous avez subis 💪.\n"+RESET,DELAY);
        delayPrint("Attention, il y a aussi une limite de vitesse qui peut être imposée et qui restreint votre progression 🚦.\n",DELAY);
        delayPrint("Planifiez bien vos coups et que le meilleur gagne ! 🎉\n",DELAY);
    }
    void delayPrint(String msg, int delay){
        for (int i = 0; i < length(msg); i++) {
            print(charAt(msg,i));
            delay(delay);
        }
    }

    int getStatPerName(NameCards nom){
        CSVFile file = loadCSV("./ressources/stats.csv");
        int i = 1;
        while (i < rowCount(file) && !equals(getCell(file,i,0) , toString(nom))) {
            i ++;
        }
        if (i != rowCount(file)) {
            return stringToInt(getCell(file,i,1));
        }else{
            return 0;
        }
    }

}