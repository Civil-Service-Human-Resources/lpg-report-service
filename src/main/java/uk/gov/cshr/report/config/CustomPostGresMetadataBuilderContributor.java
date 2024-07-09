package uk.gov.cshr.report.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators;
import org.hibernate.sql.ast.SqlAstNodeRenderingMode;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.Expression;

import java.util.List;

public class CustomPostGresMetadataBuilderContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry().register("date_trunc_tz", DateTruncFunction.INSTANCE);
    }

    public static class DateTruncFunction extends NamedSqmFunctionDescriptor {

        public static final DateTruncFunction INSTANCE = new DateTruncFunction();

        public DateTruncFunction() {
            super(
                    "date_trunc_tz",
                    false,
                    StandardArgumentsValidators.exactly(3),
                    null
            );
        }

        @Override
        public void render(SqlAppender sqlAppender,
                List<? extends SqlAstNode> arguments,
                ReturnableType<?> returnType,
                SqlAstTranslator<?> walker) {
            Expression delimiter = (Expression) arguments.get(0);
            Expression timestamp = (Expression) arguments.get(1);
            Expression timezone = (Expression) arguments.get(2);
            sqlAppender.appendSql("date_trunc(");
            walker.render(delimiter, SqlAstNodeRenderingMode.DEFAULT);
            sqlAppender.appendSql(", (");
            walker.render(timestamp, SqlAstNodeRenderingMode.DEFAULT);
            sqlAppender.appendSql(" AT TIME ZONE ");
            walker.render(timezone, SqlAstNodeRenderingMode.DEFAULT);
            sqlAppender.appendSql("))");
        }
    }
}
