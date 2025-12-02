# Mille Borne pour un champion

**Développé par :** Ylann Wattrelos et Ilies Harkou  
**Contacts :** ylann.wattrelos.etu@univ-lille.fr, ilies.harkou.etu@univ-lille.fr

---

## Présentation de Mille Borne pour un champion

_Mille Borne pour un champion_ est une adaptation informatique du classique jeu de cartes « Mille Bornes ». Le jeu consiste à parcourir une distance de 1000 bornes en gérant les obstacles et événements sur votre chemin tout en réalisant des stratégies pour ralentir vos adversaires.

Des captures d'écran illustrant le fonctionnement du jeu sont disponibles dans le répertoire (shots/).

---

## Utilisation de Mille Borne pour un champion

Pour utiliser le projet, veuillez suivre les étapes suivantes :

### 1. Compilation des fichiers sources

Dans un terminal, exécutez la commande suivante :

./compile.sh


Cette commande :

- Compile les fichiers Java situés dans le répertoire `src/`.
- Génère les fichiers `.class` dans le répertoire `classes/`.

### 2. Lancement du jeu

Une fois la compilation terminée, lancez le jeu avec la commande suivante :

./run.sh

Le jeu démarre et vous pouvez commencer à jouer.

---

## Structure du projet

Voici une description de l'organisation des fichiers et répertoires du projet :

- `src/` : Contient le code source Java.
- `classes/` : Contient les fichiers compilés Java (.class).
- `shots/` : Contient des captures d'écran du jeu.
- `compile.sh` : Script permettant de compiler le code source.
- `run.sh` : Script permettant de lancer le jeu.
- `README.md` : Document de présentation et guide d'utilisation.

---

## Prérequis

Assurez-vous que votre environnement respecte les prérequis suivants :

- **Java JDK** version 8 ou ultérieure installée.
- Un système Unix/Linux ou WSL sous Windows.
- Les droits d'exécution pour les scripts `compile.sh` et `run.sh` (si nécessaire, utilisez `chmod +x compile.sh run.sh`).

---

Amusez-vous bien avec _Mille Borne pour un champion_ !
