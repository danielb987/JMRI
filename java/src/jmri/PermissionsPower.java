package jmri;


import org.openide.util.lookup.ServiceProvider;

/**
 * Permissions for turning power on/off.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class PermissionsPower {

    public static final PermissionOwner PERMISSION_OWNER_POWER =
            new PermissionOwnerPower();

    public static final Permission PERMISSION_CONTROL_POWER =
            new PermissionControlPower();


    @ServiceProvider(service = PermissionFactory.class)
    public static class Factory implements PermissionFactory {

        @Override
        public void register(PermissionManager manager) {
            manager.registerOwner(PERMISSION_OWNER_POWER);
            manager.registerPermission(PERMISSION_CONTROL_POWER);
        }

    }


    public static class PermissionOwnerPower implements PermissionOwner {

        @Override
        public String getName() {
            return Bundle.getMessage("PermissionsPower_PermissionOwnerPower");
        }

    }

    public static class PermissionControlPower implements BooleanPermission {

        @Override
        public PermissionOwner getOwner() {
            return PERMISSION_OWNER_POWER;
        }

        @Override
        public String getName() {
            return Bundle.getMessage("PermissionsPower_PermissionControlPower");
        }

        @Override
        public BooleanValue getDefaultPermission(Role role) {
            return BooleanValue.get(role.isAdminRole());
        }

    }

    public static class PermissionProgrammingOnMain implements BooleanPermission {

        @Override
        public PermissionOwner getOwner() {
            return PERMISSION_OWNER_POWER;
        }

        @Override
        public String getName() {
            return Bundle.getMessage("PermissionsPower_PermissionProgrammingOnMain");
        }

        @Override
        public BooleanValue getDefaultPermission(Role role) {
            return BooleanValue.get(role.isAdminRole());
        }

    }

    // This class should never be instantiated.
    private PermissionsPower() {}


}
