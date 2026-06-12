# 🏎️ Mille Borne pour un champion

**Développé par :** Ylann Wattrelos et Ilies Harkou  
**Contacts :** ylann.wattrelos.etu@univ-lille.fr, ilies.harkou.etu@univ-lille.fr

---

## 📖 Présentation du projet

*Mille Borne pour un champion* est une adaptation informatique ludo-pédagogique du classique jeu de cartes « Mille Bornes ». Jouable entièrement dans le terminal, le jeu consiste à parcourir une distance de 1000 bornes en gérant les obstacles et événements sur votre chemin tout en réalisant des stratégies pour ralentir vos adversaires.

*(Des captures d'écran illustrant le fonctionnement du jeu sont disponibles dans le répertoire `shots/`)*
![Capture d'écran du jeu](shots/nom_de_ton_image_principale.png)

---

## ⚙️ Analyse technique & Défis relevés

Développer ce jeu en console (en **iJava**) a impliqué une gestion stricte des règles et des tours de jeu. Voici les principaux défis techniques relevés lors du développement :

* **Moteur de jeu au tour par tour :** Création d'une boucle de jeu principale qui gère la distribution des cartes, les actions des joueurs et la vérification des conditions de victoire.
* **Système d'états et de conditions :**
    * *Attaques et Parades :* Un joueur ne peut avancer que s'il a un feu vert ou s'il a contré une attaque (ex: Roue de secours après une Crevaison).
    * *Les Bottes (Immunité) :* Implémentation d'une logique prioritaire empêchant certaines attaques d'affecter un joueur possédant la botte correspondante.
* **Interface textuelle (CLI) :** Formatage des affichages dans la console pour que le joueur comprenne facilement sa main, les kilomètres parcourus et son statut actuel.

---

## 📂 Structure du projet

* `src/` : Contient le code source Java / iJava.
* `classes/` : Contient les fichiers compilés (.class).
* `shots/` : Contient des captures d'écran du jeu.
* `compile.sh` : Script permettant de compiler le code source.
* `run.sh` : Script permettant de lancer le jeu.

---

## 🚀 Prérequis et Installation

Assurez-vous que votre environnement respecte les prérequis suivants :
- **Java JDK** version 8 ou ultérieure installée.
- Un système Unix/Linux ou WSL sous Windows.
- Les droits d'exécution pour les scripts (si nécessaire, utilisez `chmod +x compile.sh run.sh`).

### 1. Compilation des fichiers sources

Dans un terminal, à la racine du projet, exécutez :
```bash
./compile.sh
