package vsauko.mineplayapi.api.scoreboard.animation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vsauko.mineplayapi.api.scoreboard.ScoreboardDisplayAnimation;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public class ScoreboardDisplayStaticAnimation implements ScoreboardDisplayAnimation {

    private final String currentDisplay;

    @Override
    public Collection<String> getDisplayAnimation() {
        return Collections.singleton(currentDisplay);
    }

    @Override
    public void nextDisplay() { }
}
