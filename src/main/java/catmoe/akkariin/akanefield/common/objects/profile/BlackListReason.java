package catmoe.akkariin.akanefield.common.objects.profile;

import catmoe.akkariin.akanefield.common.utils.MessageManager;

public enum BlackListReason {
    CHECK_FAILS(MessageManager.reasonCheck),
    TOO_MUCH_NAMES(MessageManager.reasonTooManyNicks),
    TOO_MUCH_JOINS(MessageManager.reasonTooManyJoins),
    ADMIN(MessageManager.reasonAdmin),
    STRANGE_PLAYER(MessageManager.reasonStrangePlayer),
    VPN(MessageManager.reasonVPN);

    private final String reason;

    BlackListReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
