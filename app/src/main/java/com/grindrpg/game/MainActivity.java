package com.grindrpg.game;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView web = new WebView(this);
        web.setWebViewClient(new WebViewClient());

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        setContentView(web);

        String html = "<!DOCTYPE html>"
            + "<html lang='ko'><head>"
            + "<meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no'>"
            + "<style>"
            + "*{box-sizing:border-box}"
            + "body{margin:0;background:#101522;color:white;font-family:sans-serif;padding:16px}"
            + "h1{color:#7ee787;text-align:center}"
            + ".box{background:#202a3c;padding:14px;margin:10px 0;border-radius:14px}"
            + "button{background:#3978ed;color:white;border:0;border-radius:9px;padding:12px;margin:4px;font-size:15px}"
            + "button:active{background:#2454ad}"
            + ".monster{width:95%;display:block;margin:6px auto}"
            + ".bar{height:15px;background:#424b5e;border-radius:10px;overflow:hidden}"
            + "#hpbar{height:100%;background:#ef5350;width:100%}"
            + ".green{color:#7ee787}.yellow{color:#ffd866}"
            + "</style></head><body>"
            + "<h1>⚔️ 노가다 RPG</h1>"
            + "<div class='box'>"
            + "<div>레벨: <b id='lv'>1</b></div>"
            + "<div>XP: <span id='xp'>0</span> / <span id='need'>10</span></div>"
            + "<div class='bar'><div id='hpbar'></div></div>"
            + "<p>공격력: <b id='atk'>1</b></p>"
            + "<p>골드: <b id='gold'>0</b></p>"
            + "</div>"
            + "<div class='box'>"
            + "<h3>몬스터 선택</h3>"
            + "<button class='monster' onclick='choose(0)'>🟢 슬라임 · HP 10</button>"
            + "<button class='monster' onclick='choose(1)'>🐌 달팽이 · HP 50</button>"
            + "<button class='monster' onclick='choose(2)'>👺 고블린 · HP 100</button>"
            + "<button class='monster' onclick='choose(3)'>🪨 오크 · HP 500</button>"
            + "<button class='monster' onclick='choose(4)'>🐉 드래곤 · HP 2000</button>"
            + "</div>"
            + "<div class='box' style='text-align:center'>"
            + "<h2 id='name'>슬라임</h2>"
            + "<div style='font-size:65px' id='emoji'>🟢</div>"
            + "<p>몬스터 HP: <b id='hp'>10</b> / <span id='maxhp'>10</span></p>"
            + "<button onclick='hit()'>⚔️ 공격하기</button>"
            + "<p id='msg'>몬스터를 공격해 보자!</p>"
            + "</div>"
            + "<div class='box'>"
            + "<h3>자동 공격 업그레이드</h3>"
            + "<p>공격 간격: <b id='speed'>1</b>초</p>"
            + "<p>업그레이드 비용: <b id='cost'>20</b> 골드</p>"
            + "<button onclick='upgrade()'>자동 공격 강화</button>"
            + "<button onclick='toggleAuto()' id='autoBtn'>자동 공격 켜기</button>"
            + "</div>"
            + "<script>"
            + "const monsters=["
            + "{n:'슬라임',e:'🟢',h:10,x:3,g:5},"
            + "{n:'달팽이',e:'🐌',h:50,x:10,g:12},"
            + "{n:'고블린',e:'👺',h:100,x:20,g:25},"
            + "{n:'오크',e:'🪨',h:500,x:70,g:80},"
            + "{n:'드래곤',e:'🐉',h:2000,x:250,g:300}"
            + "];"
            + "let lv=1,xp=0,gold=0,baseAtk=1,mi=0,hp=10,maxhp=10;"
            + "let speeds=[1,0.5,0.3,0.2,0.15,0.1],si=0,auto=false,timer=null;"
            + "function attack(){return Math.max(1,Math.floor(baseAtk*Math.pow(1.1,lv-1)))}"
            + "function render(){"
            + "document.getElementById('lv').textContent=lv;"
            + "document.getElementById('xp').textContent=xp;"
            + "document.getElementById('need').textContent=lv*10;"
            + "document.getElementById('atk').textContent=attack();"
            + "document.getElementById('gold').textContent=gold;"
            + "document.getElementById('hp').textContent=hp;"
            + "document.getElementById('maxhp').textContent=maxhp;"
            + "document.getElementById('hpbar').style.width=(hp/maxhp*100)+'%';"
            + "document.getElementById('speed').textContent=speeds[si];"
            + "document.getElementById('cost').textContent=(si+1)*20;"
            + "document.getElementById('autoBtn').textContent=auto?'자동 공격 끄기':'자동 공격 켜기';"
            + "}"
            + "function choose(i){mi=i;maxhp=monsters[i].h;hp=maxhp;"
            + "document.getElementById('name').textContent=monsters[i].n;"
            + "document.getElementById('emoji').textContent=monsters[i].e;"
            + "document.getElementById('msg').textContent='새로운 몬스터 등장!';render();}"
            + "function hit(){"
            + "if(hp<=0)return;"
            + "hp=Math.max(0,hp-attack());"
            + "if(hp===0){xp+=monsters[mi].x;gold+=monsters[mi].g;"
            + "document.getElementById('msg').textContent='처치 성공! XP +'+monsters[mi].x+' / 골드 +'+monsters[mi].g;"
            + "while(xp>=lv*10){xp-=lv*10;lv++;}"
            + "maxhp=monsters[mi].h;hp=maxhp;"
            + "}else{document.getElementById('msg').textContent='공격 성공! 피해량 '+attack();}"
            + "render();}"
            + "function upgrade(){let c=(si+1)*20;"
            + "if(si>=speeds.length-1){document.getElementById('msg').textContent='자동 공격 속도가 최대야!';return;}"
            + "if(gold<c){document.getElementById('msg').textContent='골드가 부족해!';return;}"
            + "gold-=c;si++;document.getElementById('msg').textContent='자동 공격 강화 완료!';"
            + "if(auto)startAuto();render();}"
            + "function startAuto(){if(timer)clearInterval(timer);timer=setInterval(()=>{if(auto)hit();},speeds[si]*1000);}"
            + "function toggleAuto(){auto=!auto;if(auto)startAuto();else if(timer){clearInterval(timer);timer=null;}render();}"
            + "render();"
            + "</script></body></html>";

        web.loadDataWithBaseURL(
            "https://localhost/",
            html,
            "text/html",
            "UTF-8",
            null
        );
    }
}
