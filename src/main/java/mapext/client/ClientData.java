package mapext.client;

import mapext.client.gui.AbstractColorAdapter;
import mapext.client.marks.ClientMarksManager;

public record ClientData(
        ClientMarksManager clientMarksManager,
        AbstractColorAdapter colorManager) {
    public void reset() {
        clientMarksManager.reset();
    }
}
