# ChatRoom JAX-RS

Une application de chat room simple développée avec JAX-RS (Jersey) et une interface graphique Swing.

## Prérequis

- Java JDK 8 ou supérieur
- Apache Maven
- Apache Tomcat 8 ou supérieur
- Variable d'environnement CATALINA_HOME pointant vers votre installation Tomcat

## Installation

1. Clonez le dépôt :
```bash
git clone https://github.com/saliougit/chatRoom-JAXRS.git
cd chatRoom-JAXRS
```

2. Compilation et packaging :
```bash
mvn clean package
```

3. Déploiement sur Tomcat :
- Copiez le fichier `target/chatroom-jaxrs.war` dans le dossier `webapps` de Tomcat
- Renommez-le en `chatroom.war`

## Démarrage

1. Démarrer le serveur Tomcat :
```bash
# Windows
%CATALINA_HOME%\bin\startup.bat

# Linux/Mac
$CATALINA_HOME/bin/startup.sh
```

2. Vérifiez que l'application est déployée :
- Ouvrez http://localhost:8080/chatroom/
- Vous devriez voir la page d'accueil avec la liste des endpoints

3. Lancer le client :
```bash
# Avec le script bat
.\run_client.bat

# Ou avec Maven
.\run_client_mvn.bat
```

## Utilisation

1. Au démarrage du client :
   - Entrez votre nom d'utilisateur
   - L'interface principale s'affiche

2. Interface :
   - À gauche : Liste des salons disponibles
   - Au centre : Zone de chat
   - En bas : Zone de saisie des messages
   - Bouton "Nouveau Salon" : Créer un nouveau salon
   - Bouton "Envoyer" : Envoyer un message

3. Fonctionnalités :
   - Créer des salons de discussion
   - Rejoindre un salon existant
   - Envoyer des messages dans le salon actif
   - Voir les messages des autres utilisateurs en temps réel (polling toutes les secondes)

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   ├── client/          # Client GUI et REST
│   │   ├── config/          # Configuration JAX-RS
│   │   ├── models/          # Classes modèles
│   │   ├── resources/       # Endpoints REST
│   │   └── services/        # Logique métier
│   └── webapp/             # Configuration web
```

## Architecture

- **Frontend** : Application Swing Java
- **Backend** : JAX-RS avec Jersey
- **Stockage** : En mémoire (ConcurrentHashMap)
- **Communication** : REST API
- **Actualisation** : Polling (toutes les 1-5 secondes)

## Points techniques importants

1. **Gestion des données** :
   - Toutes les données sont stockées en mémoire
   - Les données sont perdues au redémarrage du serveur
   - Utilisation de ConcurrentHashMap pour la thread-safety

2. **Communication** :
   - API REST pour toutes les opérations
   - Format JSON pour les échanges
   - Endpoints : /api/users, /api/rooms, /api/messages

3. **Interface utilisateur** :
   - GUI Swing responsive
   - Actualisation automatique des messages
   - Design moderne avec thème bleu

## Limitations connues

1. Pas de persistance des données (tout est en mémoire)
2. Pas de gestion des utilisateurs connectés/déconnectés
3. Pas de système d'authentification
4. Actualisation par polling (pas de WebSocket)
5. Pas de limitation de la taille des messages
6. Pas de nettoyage automatique des vieux messages

## Tests

Pour tester l'application :

1. Lancez plusieurs instances du client :
```bash
.\run_client.bat
```

2. Créez différents utilisateurs
3. Créez plusieurs salons
4. Testez les échanges de messages entre utilisateurs
5. Vérifiez l'actualisation en temps réel

## Dépannage

1. Si le client ne démarre pas :
   - Vérifiez que toutes les dépendances sont présentes
   - Relancez avec `mvn clean compile`

2. Si vous avez une erreur 404 :
   - Vérifiez que Tomcat est démarré
   - Vérifiez que l'application est déployée
   - Testez http://localhost:8080/chatroom/api/users

3. Si les messages ne s'actualisent pas :
   - Vérifiez la connexion réseau
   - Redémarrez le client

## Support

Pour tout problème ou question, veuillez créer une issue dans le repository GitHub.
