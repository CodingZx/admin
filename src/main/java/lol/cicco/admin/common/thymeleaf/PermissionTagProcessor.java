package lol.cicco.admin.common.thymeleaf;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

import static lol.cicco.admin.common.Constants.CICCO_PERMISSION_LIST;

public class PermissionTagProcessor extends AbstractStandardConditionalVisibilityTagProcessor {

    private static final int PRECEDENCE = 300;
    private static final String ATTR_NAME = "permission";

    public PermissionTagProcessor(final TemplateMode templateMode, final String dialectPrefix) {
        super(templateMode, dialectPrefix, ATTR_NAME, PRECEDENCE);
    }

    @Override
    protected boolean isVisible(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue) {
        var p = context.getVariable(CICCO_PERMISSION_LIST);
        if(p instanceof List){
            return ((List) p).contains(attributeValue);
        }
        return false;
    }
}
