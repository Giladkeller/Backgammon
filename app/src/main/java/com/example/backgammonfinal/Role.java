package com.example.backgammonfinal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Role extends Fragment {

    private Spinner languageSpinner;
    private TextView tvRules;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // טעינת ה-Layout
        View view = inflater.inflate(R.layout.fragment_role, container, false);

        // אתחול הרכיבים
        languageSpinner = view.findViewById(R.id.languageSpinner);
        tvRules = view.findViewById(R.id.tvRules);

        setupLanguageSpinner();

        return view;
    }

    private void setupLanguageSpinner() {
        // רשימת השפות להצגה
        String[] languages = {"עברית (Hebrew)", "English"};

        // יצירת ה-Adapter עם עיצוב מותאם אישית (טקסט זהב)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, languages) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#FFD700")); // צבע זהב כשהספינר סגור
                ((TextView) v).setTextSize(16);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#3E2723")); // רקע חום כהה ברשימה
                ((TextView) v).setTextColor(Color.parseColor("#FFD700")); // טקסט זהב ברשימה
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // מאזין לבחירת שפה
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // עברית
                    tvRules.setText("מטרת המשחק: \n" +
                            "\n" +
                            "מטרת המשחק היא שהשחקן יעביר את כל כלי המשחק שלו ל 'מגרש הביתי' שלו בלוח המשחק, ולאחר מכן \"לפנותם\" מהלוח. הראשון שיעשה זאת ינצח, כמו כן ישנם מספר סוגי נצחונות.\n" +
                            "\n" +
                            "מהלך המשחק: \n" +
                            "כל שחקן זורק בתורו שתי קוביות ומזיז את הכלי/כלים שלו. השחקן מזיז את הכלים שלו עפ\"י כל קוביה בנפרד. השחקן חייב להזיז את האבנים בכל מצב שהמשטח מאפשר לו והוא אינו רשאי לנוע אחורה. כדי לנצח במשחק צריך השחקן להכניס את כל האבנים שלו ליעדו. לאחר שהגיע למצב כזה יכול השחקן להתחיל להוציא את כליו מחוץ ללוח, צעד מעבר ללוח הוא למעשה משולש 0 או 25 בהתאמה, במידה ולאחר שהתחיל שחקן להוציא את אבניו מחוץ ללוח בוצעה לכידה על אחת מאבניו לא יכול השחקן להוציא אבנים מחוץ ללוח עד אשר תוחזר האבן ליעדו.\n" +
                            "\n" +
                            "כאשר שחקן מבצע לכידה (\"אכילה\") על כלי של שחקן יריב, יזרוק השחקן היריב את הקוביות בתורו, בהתאם לתוצאות הקוביות יוכל השחקן להכניס את הכלי לבסיסו , הספירה מבוצעת החל מהמשולש הראשון ועד לשישי, כלומר אם יזרוק השחקן 1,3 יוכל להכניס את האבן למשולשים 1 או 3 במידה והוא השחור או משולשים 24 ו-22 ללבן. שחקן שלא הצליח לשחרר את חיילו הלכוד, יוותר על תורו. אין הגבלה על מספר החיילים הלכודים.\n" +
                            "\n" +
                            "טכניקה הגנתית במשחק היא לנסות ולהשאיר כמה שפחות אבנים בודדות על המשולשים, מכיוון שאבנים אלו יכולות להילכד ואז הן מתחילות את המסלול שלהן מההתחלה, כלומר מהבסיס.\n" +
                            "\n" +
                            "חוקי המשחק: \n" +
                            "את כלי המשחק אפשר להעביר רק למשולשים המכילים כבר כלים שלך או למשולשים פנויים מכלים של היריב, או למשולש בעל כלי אחד שלו (\"לכידה\") וכך השחקן יכול \"לאכול\" ליריבו את הכלי.\n" +
                            "המספרים על שתי הקוביות מייצגות שני מהלכים נפרדים, כמו כן אפשר לשלב אותם יחדו בתנאי שהדרך פנויה.\n" +
                            "שחקן שיזרוק בתורו קוביות והמספר בקובייה יהיה זהה כלומר \"דאבל\", ישחק כפליים את המספרים שיראו הקוביות.\n" +
                            "אם אפשר לעשות מהלך רק עם אופציה אחת, חובה לבצע את המהלך (לדוגמה: אם יצא 6 3, ויש לך רק 6 אחד שאתה יכול לעשות, חובה לעשות אותו ואסור לעשות 3).\n" +
                            "סוגי נצחונות: \n" +
                            "נצחון רגיל (נקודה) - השחקן שהוציא ראשון את כל האבנים שלו.\n" +
                            "מארס (שתי נקודות) - ניצחון כאשר היריב עדין לא הוציא אפילו אבן אחת.\n" +
                            "מארס טורקי (שלוש נקודות) - מארס שבו ליריב שלך יש אבנים ביעד שלך (הבסיס שלו).\n" +
                            "מארס כוכבי (ארבע נקודות) - מארס שבו ליריב שלך יש אבנים לכודות וגם אבנים ביעד שלך (הבסיס שלו) ושהוא לא הוציא אבן אחת.");
                    tvRules.setGravity(Gravity.RIGHT); // יישור לימין
                } else { // English
                    tvRules.setText("The course of the game: \n" +
                            "\n" +
                            "The object of the game is for the player to move all of their playing pieces to their 'home court' on the game board, and then \"clear\" them from the board. The first to do so wins, and there are several types of victories .\n" +
                            "\n" +
                            "The course of the game: \n" +
                            "Each player takes turns rolling two dice and moving his piece/pieces. The player moves his pieces according to each dice individually. The player must move the pieces in any position that the board allows him to, and he is not allowed to move backwards. To win the game, the player must get all of his pieces to their destination. After reaching such a position, the player can begin to move his pieces off the board. A move beyond the board is actually a triangle of 0 or 25, respectively. If, after a player has started moving his pieces off the board, a capture is made on one of his pieces, the player cannot move any pieces off the board until the stone is returned to its destination.\n" +
                            "\n" +
                            "When a player captures (\"eats\") an opposing player's piece, the opposing player will roll the dice in turn. Depending on the results of the dice, the player can place the piece in his base. The count is made from the first to the sixth triangle, meaning that if the player rolls 1,3, he can place the stone in triangles 1 or 3 if he is black, or triangles 24 and 22 for white. A player who was unable to free his captured pawn will forfeit his turn. There is no limit to the number of captured pawns.\n" +
                            "\n" +
                            "A defensive technique in the game is to try to leave as few individual stones as possible on the triangles, because these stones can be trapped and then they start their path from the beginning, that is, from the base.\n" +
                            "\n" +
                            "Rules of the game: \n" +
                            "The game pieces can only be moved to triangles that already contain your pieces, or to triangles that are free of your opponent's pieces, or to a triangle that has one of your own pieces (\"capture\"), and thus the player can \"eat\" his opponent's piece.\n" +
                            "The numbers on the two dice represent two separate moves, and they can also be combined together provided that the path is clear.\n" +
                            "A player who rolls dice in turn and the number on the dice is the same, i.e. \"double\", will play twice the numbers shown on the dice.\n" +
                            "If a move can only be made with one option, the move must be made (for example: if the roll is 6 3, and you only have one 6 that you can make, you must make it and you must not make a 3).\n" +
                            "Types of victories: \n" +
                            "Normal victory (point) - the player who first removed all of his stones.\n" +
                            "March (two points) - a victory when the opponent has not yet removed a single stone.\n" +
                            "Turkish March (three points) - A march where your opponent has stones in your target (his base).\n" +
                            "Star March (four points) - A March where your opponent has both trapped stones and stones in your target (his base) and he has not removed a single stone.");
                    tvRules.setGravity(Gravity.LEFT); // יישור לשמאל
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}