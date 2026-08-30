package dev.cdh.affiliate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimationStateTest {

    @Test
    void nextFrameAdvancesFrameAndResetsSteps() {
        AnimationState state = new AnimationState();
        assertEquals(0, state.frameNum());
        assertEquals(0, state.animationSteps());

        state.incrementAnimationSteps();
        state.nextFrame();

        assertEquals(1, state.frameNum());
        assertEquals(0, state.animationSteps());
    }

    @Test
    void resetClearsAllCounters() {
        AnimationState state = new AnimationState();
        state.incrementAnimationSteps();
        state.incrementBubbleSteps();
        state.nextFrame();
        state.nextBubbleFrame();

        state.reset();

        assertEquals(0, state.frameNum());
        assertEquals(0, state.animationSteps());
        assertEquals(0, state.bubbleFrame());
        assertEquals(0, state.bubbleSteps());
    }

    @Test
    void bubbleCountersTrackIndependently() {
        AnimationState state = new AnimationState();
        state.nextBubbleFrame();
        assertEquals(1, state.bubbleFrame());
        assertEquals(0, state.bubbleSteps());
        assertEquals(0, state.frameNum());
    }
}
