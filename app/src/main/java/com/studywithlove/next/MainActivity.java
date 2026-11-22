package com.studywithlove.next;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    private final int INK=Color.rgb(34,39,61), MUTED=Color.rgb(130,132,148), BG=Color.rgb(247,246,251), PURPLE=Color.rgb(132,105,220), LAV=Color.rgb(237,231,255), MINT=Color.rgb(220,242,231), PEACH=Color.rgb(255,233,219);
    private LinearLayout root, body, nav;
    private String page="home";
    private int minutes=25, seconds=0, sessions=0, streak=3, water=0;
    private boolean running=false;
    private Handler handler=new Handler();
    private Runnable tick;
    private TextView timerLabel;
    private android.content.SharedPreferences prefs;

    @Override public void onCreate(Bundle b){ super.onCreate(b); getWindow().setStatusBarColor(BG); getWindow().setNavigationBarColor(BG); getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); prefs=getSharedPreferences("swl_next",0); sessions=prefs.getInt("sessions",0); streak=prefs.getInt("streak",3); water=prefs.getInt("water",0); show("home"); }
    int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,1);return t;}
    LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
    LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(0);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    void pad(View v,int a){v.setPadding(dp(a),dp(a),dp(a),dp(a));}
    void gap(LinearLayout l,int h){View v=new View(this);l.addView(v,new LinearLayout.LayoutParams(1,dp(h)));}
    void card(LinearLayout parent,View child,int color,int radius,int bottom){child.setBackground(shape(color,radius));child.setPadding(dp(18),dp(17),dp(18),dp(17));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(bottom);parent.addView(child,p);}
    TextView button(String label,int color){TextView b=text(label,14,Color.WHITE,true);b.setGravity(Gravity.CENTER);b.setPadding(dp(18),dp(13),dp(18),dp(13));b.setBackground(shape(color,18));return b;}
    void show(String target){page=target;root=column();root.setBackgroundColor(BG);setContentView(root);
        LinearLayout header=row();header.setPadding(dp(22),dp(18),dp(22),dp(12));
        TextView brand=text("✿  study with love",19,INK,true);header.addView(brand,new LinearLayout.LayoutParams(0,-2,1));
        TextView avatar=text("♡",23,PURPLE,true);avatar.setGravity(Gravity.CENTER);avatar.setBackground(shape(LAV,30));header.addView(avatar,new LinearLayout.LayoutParams(dp(44),dp(44)));root.addView(header);
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);body=column();body.setPadding(dp(20),dp(10),dp(20),dp(24));scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        render();
        nav=row();nav.setBackgroundColor(Color.WHITE);nav.setPadding(dp(7),dp(8),dp(7),dp(8));
        String[][] tabs={{"home","⌂","Главная"},{"study","◷","Фокус"},{"math","∑","Математика"},{"me","♡","Моё"}};
        for(String[] tab:tabs){final String key=tab[0];LinearLayout item=column();item.setGravity(Gravity.CENTER);TextView ico=text(tab[1],21,page.equals(key)?PURPLE:MUTED,true);TextView lbl=text(tab[2],10,page.equals(key)?PURPLE:MUTED,page.equals(key));ico.setGravity(Gravity.CENTER);lbl.setGravity(Gravity.CENTER);item.addView(ico);item.addView(lbl);item.setOnClickListener(v->show(key));nav.addView(item,new LinearLayout.LayoutParams(0,dp(54),1));}
        root.addView(nav);
    }
    void title(String s,String sub){body.addView(text(s,27,INK,true));TextView t=text(sub,13,MUTED,false);t.setPadding(0,dp(5),0,dp(18));body.addView(t);}
    void render(){switch(page){case "home":home();break;case "study":study();break;case "math":math();break;default:profile();}}
    void home(){
        title("Твой тихий прогресс ✨","Маленькие шаги каждый день — уже победа.");
        LinearLayout hero=column();hero.setBackground(shape(INK,26));pad(hero,21);
        hero.addView(text("ТВОЯ ЦЕЛЬ НА СЕГОДНЯ",10,0xFFBDB2E8,true));
        TextView h=text("Учись в своём ритме.",24,Color.WHITE,true);h.setPadding(0,dp(9),0,dp(5));hero.addView(h);
        hero.addView(text("25 минут внимания могут изменить весь день.",13,0xFFD6D5E2,false));gap(hero,18);
        TextView start=button("Начать фокус  →",PURPLE);hero.addView(start,new LinearLayout.LayoutParams(-1,-2));start.setOnClickListener(v->show("study"));card(body,hero,INK,26,17);
        LinearLayout stats=row();stat(stats,"🔥",String.valueOf(streak),"дня подряд");stat(stats,"✦",String.valueOf(sessions),"фокус-сессий");stat(stats,"🌱",String.valueOf(water),"глотков для цветка");body.addView(stats);gap(body,20);
        body.addView(text("ТВОЁ ПРОСТРАНСТВО",11,MUTED,true));gap(body,10);
        feature("◷","Таймер фокуса","Работа и отдых в комфортном ритме",LAV,()->show("study"));
        feature("∑","Математика","Темы, понятные объяснения и практика",MINT,()->show("math"));
        feature("✿","Мой цветочек","Заботься о своём маленьком ростке",PEACH,()->show("me"));
        feature("▦","Мой прогресс","Смотри, как растёт твоя привычка",Color.rgb(232,239,255),()->show("me"));
    }
    void stat(LinearLayout parent,String icon,String value,String label){LinearLayout c=column();c.setBackground(shape(Color.WHITE,18));pad(c,12);c.addView(text(icon,18,INK,true));c.addView(text(value,20,INK,true));c.addView(text(label,10,MUTED,false));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.setMargins(dp(3),0,dp(3),0);parent.addView(c,p);}
    void feature(String icon,String name,String desc,int tint,Runnable action){LinearLayout c=row();c.setBackground(shape(Color.WHITE,20));c.setPadding(dp(14),dp(13),dp(14),dp(13));TextView i=text(icon,22,PURPLE,true);i.setGravity(Gravity.CENTER);i.setBackground(shape(tint,15));c.addView(i,new LinearLayout.LayoutParams(dp(48),dp(48)));LinearLayout info=column();info.setPadding(dp(12),0,0,0);info.addView(text(name,15,INK,true));info.addView(text(desc,11,MUTED,false));c.addView(info,new LinearLayout.LayoutParams(0,-2,1));c.addView(text("›",27,MUTED,false));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(10);body.addView(c,p);c.setOnClickListener(v->action.run());}
    void study(){title("Время для себя","Выбери ритм и просто начни.");
        LinearLayout timer=column();timer.setGravity(Gravity.CENTER);timer.setBackground(shape(INK,28));timer.setPadding(dp(20),dp(25),dp(20),dp(25));
        timer.addView(text("ФОКУС-СЕССИЯ",11,0xFFBDB2E8,true));timerLabel=text(String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds),56,Color.WHITE,true);timerLabel.setPadding(0,dp(12),0,dp(12));timer.addView(timerLabel);
        timer.addView(text("Без спешки. Ты справляешься.",13,0xFFD6D5E2,false));gap(timer,20);
        LinearLayout actions=row();TextView toggle=button(running?"Пауза":"Начать",PURPLE);TextView reset=button("Сбросить",0xFF454B68);LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(0,-2,1);ap.setMargins(dp(4),0,dp(4),0);actions.addView(toggle,ap);actions.addView(reset,ap);timer.addView(actions);card(body,timer,INK,28,18);
        toggle.setOnClickListener(v->{if(running)pause();else startTimer();show("study");});reset.setOnClickListener(v->{pause();minutes=25;seconds=0;show("study");});
        body.addView(text("ТВОЙ РИТМ",11,MUTED,true));gap(body,8);
        LinearLayout choices=row();for(int m:new int[]{15,25,45,60}){TextView b=button(m+" мин",m==minutes?PURPLE:0xFFAAA3C8);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.setMargins(dp(3),0,dp(3),0);choices.addView(b,p);b.setOnClickListener(v->{pause();minutes=m;seconds=0;show("study");});}body.addView(choices);gap(body,18);
        feature("☁","Перерыв тоже важен","После фокуса отдохни 5 минут",MINT,()->{pause();minutes=5;seconds=0;show("study");});
        body.addView(text("Завершено сессий: "+sessions,14,MUTED,false));
    }
    void startTimer(){if(running)return;running=true;tick=new Runnable(){public void run(){if(!running)return;if(seconds==0){if(minutes==0){running=false;sessions++;prefs.edit().putInt("sessions",sessions).apply();new AlertDialog.Builder(MainActivity.this).setTitle("Отличная работа!").setMessage("Фокус-сессия завершена. Сделай небольшой перерыв 🌿").setPositiveButton("Продолжить",null).show();return;}minutes--;seconds=59;}else seconds--;if(timerLabel!=null)timerLabel.setText(String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds));handler.postDelayed(this,1000);}};handler.postDelayed(tick,1000);}
    void pause(){running=false;if(tick!=null)handler.removeCallbacks(tick);}
    String[][] topics={{"Числа и действия","Натуральные и целые числа","Сложение и вычитание","Умножение и деление","Порядок действий"},{"Обыкновенные дроби","Что такое дробь","Сравнение дробей","Сложение и вычитание дробей","Умножение и деление дробей"},{"Десятичные дроби","Десятичная запись","Сравнение десятичных дробей","Действия с десятичными дробями"},{"Проценты и пропорции","Понятие процента","Нахождение процента от числа","Пропорции и задачи"},{"Алгебра","Буквенные выражения","Уравнения","Степени и корни"},{"Геометрия","Углы и прямые","Треугольники","Периметр и площадь"}};
    void math(){title("Математика ∑","Изучай темы постепенно и закрепляй знания.");for(int i=0;i<topics.length;i++){final int n=i;LinearLayout c=column();c.setBackground(shape(Color.WHITE,21));pad(c,16);LinearLayout r=row();r.addView(text(new String[]{"01","02","03","04","05","06"}[i],12,PURPLE,true));TextView count=text((topics[i].length-1)+" тем   ›",11,MUTED,false);r.addView(count,new LinearLayout.LayoutParams(0,-2,1));count.setGravity(Gravity.RIGHT);c.addView(r);c.addView(text(topics[i][0],18,INK,true));c.addView(text("Открой раздел и разбери каждую тему",12,MUTED,false));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(11);body.addView(c,p);c.setOnClickListener(v->topicList(n));}}
    void topicList(int n){body.removeAllViews();TextView back=text("‹  Все разделы",13,PURPLE,true);body.addView(back);back.setOnClickListener(v->show("math"));gap(body,16);title(topics[n][0],"Выбери тему для изучения");for(int i=1;i<topics[n].length;i++){final String topic=topics[n][i];feature("✧",topic,"Краткое объяснение и пример",LAV,()->lesson(topic));}}
    void lesson(String topic){body.removeAllViews();TextView back=text("‹  К темам",13,PURPLE,true);body.addView(back);back.setOnClickListener(v->show("math"));gap(body,18);title(topic,"Разберём основную идею на простом примере.");
        LinearLayout c=column();c.setBackground(shape(Color.WHITE,22));pad(c,18);c.addView(text("ГЛАВНАЯ МЫСЛЬ",10,PURPLE,true));gap(c,8);c.addView(text("В математике важно понять правило, а затем закрепить его на нескольких примерах. Записывай шаги по порядку и проверяй результат обратным действием.",15,INK,false));gap(c,16);c.addView(text("ПРИМЕР",10,PURPLE,true));gap(c,8);c.addView(text("Если a = 8 и b = 4, то a + b = 12, а a × b = 32.",17,INK,true));card(body,c,Color.WHITE,22,14);
        TextView done=button("Отметить тему изученной  ✓",PURPLE);body.addView(done);done.setOnClickListener(v->{int k=prefs.getInt("topics_done",0)+1;prefs.edit().putInt("topics_done",k).apply();done.setText("Тема отмечена ✓");});}
    void profile(){title("Моё пространство ♡","Твои привычки, маленькие победы и забота о себе.");
        LinearLayout flower=column();flower.setGravity(Gravity.CENTER);flower.setBackground(shape(PEACH,25));pad(flower,22);flower.addView(text("✿",62,PURPLE,true));flower.addView(text("Мой цветочек",21,INK,true));flower.addView(text("Полей цветок, когда завершишь учёбу",12,MUTED,false));gap(flower,12);TextView waterBtn=button("＋  Полить цветок",PURPLE);flower.addView(waterBtn);waterBtn.setOnClickListener(v->{water++;prefs.edit().putInt("water",water).apply();show("me");});flower.addView(text("Поливов: "+water,12,INK,true));card(body,flower,PEACH,25,18);
        body.addView(text("ТВОЯ СТАТИСТИКА",11,MUTED,true));gap(body,9);stat(body,"🔥",String.valueOf(streak),"дня подряд");gap(body,8);stat(body,"✦",String.valueOf(sessions),"фокус-сессий");gap(body,8);stat(body,"✓",String.valueOf(prefs.getInt("topics_done",0)),"изученных тем");gap(body,18);
        feature("▦","Календарь привычек","Отмечай дни, когда уделил время себе",MINT,()->new AlertDialog.Builder(this).setTitle("Твоя привычка").setMessage("Каждый день — новая возможность стать ближе к цели.").setPositiveButton("Хорошо",null).show());
        feature("✉","Письмо себе в будущее","Сохрани мысли и цели",LAV,()->letter());
        feature("♫","Атмосфера учёбы","Создай спокойное настроение",PEACH,()->new AlertDialog.Builder(this).setTitle("Атмосфера").setItems(new String[]{"Тихий режим","Мягкий вечер","Энергия утра"},(d,w)->{}).show());
        feature("♧","Напоминания","Не забывай о времени для себя",MINT,()->reminder());
    }
    void letter(){EditText input=new EditText(this);input.setHint("Что ты хочешь сказать себе будущему?");input.setMinLines(4);input.setGravity(Gravity.TOP);input.setText(prefs.getString("letter",""));new AlertDialog.Builder(this).setTitle("Письмо себе ✉").setView(input).setPositiveButton("Сохранить",(d,w)->prefs.edit().putString("letter",input.getText().toString()).apply()).setNegativeButton("Отмена",null).show();}
    void reminder(){new AlertDialog.Builder(this).setTitle("Напоминания").setMessage("Выдели время для учёбы каждый день. В следующем обновлении добавим гибкое расписание и выбор дней недели.").setPositiveButton("Понятно",null).show();}
    @Override protected void onDestroy(){super.onDestroy();pause();}
}
