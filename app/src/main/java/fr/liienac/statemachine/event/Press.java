/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */

package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

public class Press<Item> extends PositionalEvent {
	public Press(int cursorid_, Point p_, Item s_, float angRad) { super(/*Type.Press,*/ cursorid_, p_, s_, angRad); }
}