/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
import SvgIcon, { SvgIconProps } from '@mui/material/SvgIcon';

export const SysONIcon = (props: SvgIconProps) => {
  return (
    <SvgIcon
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 64 64"
      aria-labelledby="title"
      aria-describedby="desc"
      role="img"
      {...props}>
      <g clipPath="url(#clip0)">
        <path d="M34.71,34.43v29.16l22.88-12.36c3.11-1.68,5.05-4.93,5.05-8.47V19.47L34.71,34.43z" />
        <path
          d="M26.8,37.47v22.16l-19-10.26c-2.46-1.33-3.99-3.89-3.99-6.69V23.55l21.15,11.42l2.61-1.38L1.36,19.43v23.25
            c0,3.7,2.02,7.09,5.28,8.85l22.62,12.21V36.17L26.8,37.47z"
        />
        <path
          d="M54.53,17.03L31.98,29.12L6.57,15.49L28.26,3.66c2.33-1.27,5.11-1.27,7.44,0l18.87,10.29h5.12L36.87,1.5
            c-3.06-1.67-6.72-1.67-9.79,0L1.41,15.51l30.57,16.4l27.74-14.88H54.53z"
        />
      </g>
      <defs>
        <clipPath id="clip0">
          <rect width="64" height="64" />
        </clipPath>
      </defs>
    </SvgIcon>
  );
};
