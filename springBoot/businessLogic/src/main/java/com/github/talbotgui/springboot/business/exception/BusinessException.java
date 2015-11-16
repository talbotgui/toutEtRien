package com.github.talbotgui.springboot.business.exception;

import com.github.talbotgui.springboot.business.exception.ExceptionId.ExceptionLevel;

/**
 *
 * 400 : BAD REQUEST - La syntaxe de la requête est erronée
 *
 * 409 : CONFLICT - La requête ne peut être traitée à l’état actuel
 */
public class BusinessException extends RuntimeException {

	public static final ExceptionId CLIENT_NOM_DEJA_EXISTANT = new ExceptionId("CLIENT_NOM_DEJA_EXISTANT",
			"Un client portant ce nom existe deja (nom={0}, prenom={1}).", ExceptionLevel.ERROR, 409);

	public static final ExceptionId CLIENT_NOM_ET_PRENOM_OBLIGATOIRES = new ExceptionId(
			"CLIENT_NOM_ET_PRENOM_OBLIGATOIRES",
			"Le nom et le prenom d'un client sont obligatoires (nom={0}, prenom={1}).", ExceptionLevel.ERROR, 400);

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public static boolean equals(Exception e, ExceptionId id) {
		if (!BusinessException.class.isInstance(e)) {
			return false;
		}
		return ((BusinessException) e).getExceptionId().equals(id);
	}

	/** Exception identifier. */
	private ExceptionId exceptionId;

	/** Message parameters. */
	private String[] parameters;

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	public BusinessException(ExceptionId exceptionId) {
		super();
		this.setExceptionId(exceptionId);
	}

	/**
	 * Constructor.
	 *
	 * @param pExceptionId
	 *            Exception identifier.
	 * @param pParameters
	 *            Message parameters.
	 */
	public BusinessException(ExceptionId pExceptionId, String[] pParameters) {
		this(pExceptionId);
		this.setParameters(pParameters);
	}

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 * @param nestedException
	 *            Embedded exception.
	 */
	public BusinessException(ExceptionId exceptionId, Throwable nestedException) {
		super(nestedException);
		this.setExceptionId(exceptionId);
	}

	/**
	 * Constructor.
	 *
	 * @param pExceptionId
	 *            Exception identifier.
	 * @param pNestedException
	 *            Embedded exception.
	 * @param pParameters
	 *            Message parameters.
	 */
	public BusinessException(ExceptionId pExceptionId, Throwable pNestedException, String[] pParameters) {
		super(pNestedException);
		this.setExceptionId(pExceptionId);
		this.setParameters(pParameters);
	}

	/**
	 * GETTER.
	 *
	 * @return Exception identifier.
	 */
	public ExceptionId getExceptionId() {
		return this.exceptionId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		String result;
		if (this.exceptionId != null) {
			String message = this.exceptionId.getDefaultMessage();
			if (message != null && this.parameters != null) {
				for (int i = 0; i < this.parameters.length; i++) {
					if (this.parameters[i] != null) {
						message = message.replace("{" + i + "}", this.parameters[i]);
					}
				}
			}
			result = message;
		} else {
			result = super.getMessage();
		}
		return result;
	}

	/**
	 * GETTER.
	 *
	 * @return Message parameters.
	 */
	public String[] getParameters() {
		return this.parameters;
	}

	/**
	 * SETTER.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	private void setExceptionId(ExceptionId exceptionId) {
		this.exceptionId = exceptionId;
	}

	/**
	 * SETTER.
	 *
	 * @param pParameters
	 *            Message parameters.
	 */
	public void setParameters(String[] pParameters) {
		this.parameters = pParameters;
	}
}
