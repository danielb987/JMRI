package apps.startup;

import jmri.util.startup.AbstractActionModelFactory;
import jmri.util.startup.StartupModelFactory;
import jmri.util.startup.StartupModel;

import apps.CreateButtonModel;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Randall Wood 2016
 */
@ServiceProvider(service = StartupModelFactory.class)
public class CreateButtonModelFactory extends AbstractActionModelFactory {

    public CreateButtonModelFactory() {
    }

    @Override
    public Class<? extends StartupModel> getModelClass() {
        return CreateButtonModel.class;
    }

    @Override
    public CreateButtonModel newModel() {
        return new CreateButtonModel();
    }

    @Override
    public String getEditModelMessage() {
        return Bundle.getMessage("CreateButtonModelFactory.editModel.message");
    }
}
