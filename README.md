# Personal Relationship Management
Android app to stay in touch with my friends.

## O aplikaci

Také se vám stává, že přicházíte o nabyté kontakty a kamarádství?
Cestujete po světě, ale nejste schopni udržet navázaná přátelství?

Personal Relationship Management je přímo pro vás!

Zapisujte si kontakty a zaznamenávejte si, kdy jste naposledy danou osobu kontaktovali.
Aplikace automaticky řadí nejstarší kontaktované osoby dopředu.
Ke všem kontaktům si můžete přidat zemi, způsob kontaktu, kategorii i další poznámky.

V přehledu kontaktů pak lze jednoduše nastavovat nové datum posledního kontaktu po kliknutí na ikonu kalendáře.
Pro nastavení dnešního data jako posledního kontaktu pak stačí položku kontaktu dlouze podržet.

Po přihlášení pomocí Google je možné kontakty uložit na server a synchronizovat mezi zařízeními.

## Implementace

Pro ukládání kontaktů je použita lokální Room databáze.
Zálohování je umožněno díky Firebase Authentiaction a Firebase Realtime Database.
Jako DI framework je použit KOIN.
UI je v Jetpack Compose včetně navigace.
