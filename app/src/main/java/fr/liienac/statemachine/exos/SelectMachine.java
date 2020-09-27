package fr.liienac.statemachine.exos;

import java.util.ArrayList;
import java.util.List;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.graphic.Item;

public class SelectMachine extends StateMachine {
    Item graphicItem = null;
    List<Integer> listCursors = new ArrayList<>();

    public SelectMachine(Item graphicItem) {
        this.graphicItem = graphicItem;
    }

    public State start = new State() {
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size() == 0;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.add(evt.cursorID);
            }

            public State goTo() {
                return touched;
            }
        };
    };

    public State touched = new State() {

        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem ;
            }

            public void action() {
                listCursors.add(evt.cursorID);
            }

            public State goTo() {
                return touched;
            }
        };


        Transition releaseStay = new Transition<Release>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.size() > 1;
            }

            public void action() {
                listCursors.remove((Object) evt.cursorID);
            }

            public State goTo() {
                return touched;
            }
        };

        Transition releaseStart = new Transition<Release>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.size() == 1;
            }

            public void action() {
                graphicItem.style.r = 0;
                listCursors.remove((Object) evt.cursorID);
            }

            public State goTo() {
                return start;
            }
        };


    };
}